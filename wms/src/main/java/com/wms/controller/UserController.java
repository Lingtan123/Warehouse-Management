package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.User;
import com.wms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author nobody
 * @since 2026-05-26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("")
    public String FirstPage(){
        return "This is FirstPage";
    }


    @Autowired
    private IUserService userService;
    @GetMapping("/list")
    public Result listAll(){
        return Result.success(userService.listAll());
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.save(user) ? Result.success() : Result.fail();
    }
    //修改
    @PostMapping("/mod")
    public Result mod(@RequestBody User user){
        return userService.updateById(user) ? Result.success() : Result.fail();
    }
    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    //删除
    @GetMapping("/delete")
    public Result delete(int id){
        return userService.removeById(id) ? Result.success() : Result.fail();
    }

    //根据账号查询
    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
        List<User> list = userService.lambdaQuery().eq(User::getNo, no).list();
        return !list.isEmpty() ? Result.success(list) : Result.fail();
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        List<User> list =  userService.lambdaQuery().eq(User::getNo, user.getNo())
                .eq(User::getPassword,user.getPassword())
                .list();
        return !list.isEmpty() ? Result.success(list) : Result.fail();
    }

    //查询（模糊、匹配）
    @PostMapping("/listP")
    public Result listP(@RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        if(user.getName() != null){
            queryWrapper.like(User::getName, user.getName());
        }
       // queryWrapper.like(User::getName,user.getName());
        //like模糊匹配 eq精确匹配
        //queryWrapper.eq(User::getName,user.getName());
        return Result.success(userService.list(queryWrapper));
    }

    //分页查询
    //直接查询可以直接新增查询条件，通过map.get()获取即可
    @PostMapping("/listPage")
    public Result listPage(@RequestBody HashMap map){
        System.out.println(map);
        int pageNum = map.get("pageNum") == null ? 1 : ((Number)map.get("pageNum")).intValue();
        int pageSize = map.get("pageSize") == null ? 10 : ((Number)map.get("pageSize")).intValue();
        String name = map.get("name").toString();
        String id = map.get("id").toString();
        Page<User> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getName,name);
        queryWrapper.eq(User::getId,id);

        IPage iPage = userService.page(page, queryWrapper);
        System.out.println("Total = " + iPage.getTotal());

        return Result.success(iPage.getRecords());
    }
    //分页查询（自定义Page对象）
    //自定义Page对象可以不修改自定义的实体类，使用Hashmap封装所有数据，然后再通过map.get()调用
    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam query){
        System.out.println(query);
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name =(String) param.get("name");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getName,name);

        IPage iPage = userService.page(page, queryWrapper);

        System.out.println("total == "+ iPage.getTotal());

        return Result.success(iPage.getRecords());
    }

    //分页查询（自定义SQL语句）
    @PostMapping("/listPageCC")
    public Result listPageCC(@RequestBody QueryPageParam query){
        System.out.println(query);
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        String name =(String) param.get("name");
        queryWrapper.like(User::getName,name);

        IPage iPage = userService.pageCC(page,queryWrapper);
        System.out.println("total == "+ iPage.getTotal());
        return Result.success(iPage.getRecords());
    }

    //分页查询（后端返回数据封装）（和自定义分页使用同一份返回）
    //使用自定义类Result封装
    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query){
        System.out.println(query);
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name =(String) param.get("name");
        String sex =(String) param.get("sex");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        if(StringUtils.isNotBlank(name)){
            queryWrapper.like(User::getName,name);
        }
        if(StringUtils.isNotBlank(sex)){
            queryWrapper.like(User::getSex,sex);
        }

        IPage iPage = userService.page(page, queryWrapper);

        System.out.println("total == "+ iPage.getTotal());

        return Result.success(iPage.getTotal(),iPage.getRecords());
    }
}
