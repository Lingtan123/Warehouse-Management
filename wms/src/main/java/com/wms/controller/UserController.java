package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
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
    public List<User> listAll(){
        return userService.listAll();
    }

    //新增
    @PostMapping("/save")
    public boolean save(@RequestBody User user){
        return userService.save(user);
    }
    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user){
        return userService.updateById(user);
    }
    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    //删除
    @GetMapping("/delete")
    public boolean delete(int id){
        return userService.removeById(id);
    }

    //查询（模糊、匹配）
    @PostMapping("/listP")
    public List<User> listP(@RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.like(User::getName,user.getName());
        //like模糊匹配 eq精确匹配
        //queryWrapper.eq(User::getName,user.getName());
        return userService.list(queryWrapper);
    }

    //分页查询
    @PostMapping("/listPage")
    public List<User> listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name =(String) param.get("name");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getName,name);

        IPage result = userService.page(page, queryWrapper);

        System.out.println("total == "+result.getTotal());

        return result.getRecords();
    }
    //分页查询（自定义Page对象）
    @PostMapping("/listPageC")
    public List<User> listPageC(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name =(String) param.get("name");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getName,name);

        IPage result = userService.page(page, queryWrapper);

        System.out.println("total == "+result.getTotal());

        return result.getRecords();
    }

}
