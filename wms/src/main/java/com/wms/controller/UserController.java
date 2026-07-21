package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.auth.CurrentUser;
import com.wms.auth.JwtUtils;
import com.wms.auth.UserContext;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.User;
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
    public Result save(@RequestBody User user) {
        boolean saved = userService.save(user);
        if (saved) {
            log.info("event=CREATE_USER operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                    operatorId(), operatorNo(), operatorName(), user.getId(), user.getNo(), user.getName(), user.getRoleId());
            return Result.success();
        }

        log.warn("event=CREATE_USER_FAIL operatorId={} operatorNo={} operatorName={} targetUserNo={} targetUserName={} targetRoleId={}",
                operatorId(), operatorNo(), operatorName(), user.getNo(), user.getName(), user.getRoleId());
        return Result.fail();
    }

    @PostMapping("/mod")
    public Result mod(@RequestBody User user) {
        boolean updated = userService.updateById(user);
        if (updated) {
            log.info("event=UPDATE_USER operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                    operatorId(), operatorNo(), operatorName(), user.getId(), user.getNo(), user.getName(), user.getRoleId());
            return Result.success();
        }

        log.warn("event=UPDATE_USER_FAIL operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                operatorId(), operatorNo(), operatorName(), user.getId(), user.getNo(), user.getName(), user.getRoleId());
        return Result.fail();
    }

    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        User targetUser = userService.getById(id);
        boolean removed = userService.removeById(id);
        if (removed) {
            log.info("event=DELETE_USER operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                    operatorId(), operatorNo(), operatorName(),
                    targetUser == null ? id : targetUser.getId(),
                    targetUser == null ? null : targetUser.getNo(),
                    targetUser == null ? null : targetUser.getName(),
                    targetUser == null ? null : targetUser.getRoleId());
            return Result.success();
        }

        log.warn("event=DELETE_USER_FAIL operatorId={} operatorNo={} operatorName={} targetUserId={} targetUserNo={} targetUserName={} targetRoleId={}",
                operatorId(), operatorNo(), operatorName(),
                targetUser == null ? id : targetUser.getId(),
                targetUser == null ? null : targetUser.getNo(),
                targetUser == null ? null : targetUser.getName(),
                targetUser == null ? null : targetUser.getRoleId());
        return Result.fail();
    }

    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no) {
        List<User> list = userService.lambdaQuery().eq(User::getNo, no).list();
        return !list.isEmpty() ? Result.success(list) : Result.fail();
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        List<User> list = userService.lambdaQuery().eq(User::getNo, user.getNo())
                .eq(User::getPassword, user.getPassword())
                .list();
        if (!list.isEmpty()) {
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

        log.warn("event=LOGIN_FAIL no={} reason=invalid_credentials", user.getNo());
        return Result.fail();
    }

    @PostMapping("/listP")
    public Result listP(@RequestBody User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (user.getName() != null) {
            queryWrapper.like(User::getName, user.getName());
        }
        return Result.success(userService.list(queryWrapper));
    }

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

    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name = (String) param.get("name");
        String sex = (String) param.get("sex");
        String roleId = (String) param.get("roleId");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(User::getName, name);
        }
        if (StringUtils.isNotBlank(sex)) {
            queryWrapper.like(User::getSex, sex);
        }
        if (StringUtils.isNotBlank(roleId)) {
            queryWrapper.like(User::getRoleId, roleId);
        }

        IPage<User> iPage = userService.page(page, queryWrapper);
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
}
