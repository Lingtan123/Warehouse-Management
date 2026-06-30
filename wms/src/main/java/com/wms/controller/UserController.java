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
    //直接查询可以直接新增查询条件，通过map.get()获取即可
    @PostMapping("/listPage")
    public List<User> listPage(@RequestBody HashMap map){
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

        return iPage.getRecords();
    }
    //分页查询（自定义Page对象）
    //自定义Page对象可以不修改自定义的实体类，使用Hashmap封装所有数据，然后再通过map.get()调用
    @PostMapping("/listPageC")
    public List<User> listPageC(@RequestBody QueryPageParam query){
        System.out.println(query);
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name =(String) param.get("name");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getName,name);

        IPage iPage = userService.page(page, queryWrapper);

        System.out.println("total == "+ iPage.getTotal());

        return iPage.getRecords();
    }

    //分页查询（自定义SQL语句）
    @PostMapping("/listPageCC")
    public List<User> listPageCC(@RequestBody QueryPageParam query){
        System.out.println(query);
        HashMap param = query.getParam();
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        String name =(String) param.get("name");
        queryWrapper.like(User::getName,name);

        IPage iPage = userService.pageCC(page,queryWrapper);
        System.out.println("total == "+ iPage.getTotal());
        return iPage.getRecords();
    }
}
