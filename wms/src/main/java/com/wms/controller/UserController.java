package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.auth.CurrentUser;
import com.wms.auth.JwtUtils;
import com.wms.auth.UserContext;
import com.wms.dto.QueryPageParam;
import com.wms.common.Result;
import com.wms.dto.LoginRequest;
import com.wms.dto.UserQuery;
import com.wms.dto.UserRequest;
import com.wms.entity.Menu;
import com.wms.entity.User;
import com.wms.exception.AllException;
import com.wms.exception.myException;
import com.wms.service.IMenuService;
import com.wms.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("")
    public String firstPage() {
        return "This is FirstPage";
    }

    @GetMapping("/list")
    public Result listAll() {
        return Result.success(userService.listAll());
    }

    @PostMapping("/save")
    public Result save(@RequestBody UserRequest user) {
        if (!userService.createUser(user)) {
            throw new myException(AllException.USER_INSERT_ERROR);
        }
        log.info("event=CREATE_USER operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                operatorId(), operatorNo(), operatorName(), user.getId(), user.getNo(), user.getName(), user.getRoleId());
        return Result.success();
    }

    @PostMapping("/mod")
    public Result mod(@RequestBody UserRequest user) {
        if (!userService.updateUser(user)) {
            throw new myException(AllException.USER_UPDATE_ERROR);
        }
        log.info("event=UPDATE_USER operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                operatorId(), operatorNo(), operatorName(), user.getId(), user.getNo(), user.getName(), user.getRoleId());
        return Result.success();
    }

    @PostMapping("/saveOrMod")
    public Result saveOrMod(@RequestBody UserRequest user) {
        boolean isCreate = user.getId() == null;
        if (!userService.saveOrUpdateUser(user)) {
            throw new myException(isCreate ? AllException.USER_INSERT_ERROR : AllException.USER_UPDATE_ERROR);
        }
        if (isCreate) {
            log.info("event=CREATE_USER operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                    operatorId(), operatorNo(), operatorName(), user.getId(), user.getNo(), user.getName(), user.getRoleId());
        } else {
            log.info("event=UPDATE_USER operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                    operatorId(), operatorNo(), operatorName(), user.getId(), user.getNo(), user.getName(), user.getRoleId());
        }
        return Result.success();
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        User targetUser = userService.getById(id);
        if (!userService.removeById(id)) {
            throw new myException(AllException.USER_DELETE_ERROR);
        }
        log.info("event=DELETE_USER operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                operatorId(), operatorNo(), operatorName(),
                targetUser == null ? id : targetUser.getId(),
                targetUser == null ? null : targetUser.getNo(),
                targetUser == null ? null : targetUser.getName(),
                targetUser == null ? null : targetUser.getRoleId());
        return Result.success();
    }

    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no) {
        List<User> list = userService.lambdaQuery().eq(User::getNo, no).list();
        return !list.isEmpty() ? Result.success(list) : Result.fail();
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest user) {
        List<User> list = userService.lambdaQuery().eq(User::getNo, user.getNo())
                .eq(User::getPassword, user.getPassword())
                .list();
        if (list.isEmpty()) {
            log.warn("event=LOGIN_FAIL no={} reason=invalid_credentials", user.getNo());
            throw new myException(AllException.LOGIN_ERROR);
        }

        User loginUser = list.get(0);
        List<Menu> menuList = menuService.lambdaQuery().like(Menu::getMenuRight, loginUser.getRoleId()).list();
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", jwtUtils.generateToken(loginUser));
        map.put("user", loginUser);
        map.put("menu", menuList);

        log.info("event=LOGIN_SUCCESS userId={} no={} name={} roleId={}",
                loginUser.getId(), loginUser.getNo(), loginUser.getName(), loginUser.getRoleId());
        return Result.success(map);
    }

    /*
    * 以下部分是学习用代码，前端不进行访问
    * */

    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody UserQuery query) {
        IPage<User> iPage = userService.listPageC1(query);
        return Result.success(iPage.getTotal(), iPage.getRecords());
    }

    private Integer operatorId() {
        CurrentUser currentUser = UserContext.getCurrentUser();
        return currentUser == null ? null : currentUser.getId();
    }

    private String operatorNo() {
        CurrentUser currentUser = UserContext.getCurrentUser();
        return currentUser == null ? null : currentUser.getNo();
    }

    private String operatorName() {
        CurrentUser currentUser = UserContext.getCurrentUser();
        return currentUser == null ? null : currentUser.getName();
    }


    //用like模糊匹配，eq精确匹配
    @PostMapping("/listP")
    public Result listP(@RequestBody UserRequest user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (user.getName() != null) {
            queryWrapper.like(User::getName, user.getName());
        }
        return Result.success(userService.list(queryWrapper));
    }

    //分页查询，用HashMap装页数和每页条数，还需要装查询关键字
    //页数和每页条数具体指的是，所有匹配的结果变成一张表，按照每页条数划分，选取页数对应的那一块
    @PostMapping("/listPage")
    public Result listPage(@RequestBody HashMap map) {
        int pageNum = map.get("pageNum") == null ? 1 : ((Number) map.get("pageNum")).intValue();
        int pageSize = map.get("pageSize") == null ? 10 : ((Number) map.get("pageSize")).intValue();
        String name = map.get("name") == null ? "" : map.get("name").toString();
        String id = map.get("id") == null ? "" : map.get("id").toString();
        Page<User> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getName, name);
        queryWrapper.eq(User::getId, id);

        IPage<User> iPage = userService.page(page, queryWrapper);
        return Result.success(iPage.getRecords());
    }

    //使用自定义封装对象封装查询请求
    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name = (String) param.get("name");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getName, name);

        IPage<User> iPage = userService.page(page, queryWrapper);
        return Result.success(iPage.getRecords());
    }
    //Page<User> page只记录分页的参数
    //LambdaQueryWrapper只负责添加查询条件，相当于sql语句
    //真正的查询由service接口方法执行
    //IPage<User> iPage负责保存查询的结果和分页信息，getRecords()取出当前数据页列表
    @PostMapping("/listPageCC")
    public Result listPageCC(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        String name = (String) param.get("name");
        queryWrapper.like(User::getName, name);

        IPage<User> iPage = userService.pageCC(page, queryWrapper);
        return Result.success(iPage.getRecords());
    }
}
