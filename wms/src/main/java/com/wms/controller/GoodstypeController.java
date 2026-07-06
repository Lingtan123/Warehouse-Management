package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goodstype;
import com.wms.entity.Storage;
import com.wms.service.IGoodstypeService;
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
 * @since 2026-07-06
 */
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private IGoodstypeService goodstypeService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype){
        return goodstypeService.save(goodstype) ? Result.success() : Result.fail();
    }
    //修改
    @PostMapping("/mod")
    public Result mod(@RequestBody Goodstype goodstype){
        return goodstypeService.updateById(goodstype) ? Result.success() : Result.fail();
    }
    //删除
    @GetMapping("/delete")
    public Result delete(int id){
        return goodstypeService.removeById(id) ? Result.success() : Result.fail();
    }
    //分页查询（后端返回数据封装）（和自定义分页使用同一份返回）
    //使用自定义类Result封装
    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam query){
        System.out.println(query);
        HashMap param = query.getParam();
        Page<Goodstype> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name =(String) param.get("name");

        LambdaQueryWrapper<Goodstype> queryWrapper = new LambdaQueryWrapper<>();

        if(StringUtils.isNotBlank(name)){
            queryWrapper.like(Goodstype::getName,name);
        }

        IPage iPage = goodstypeService.page(page, queryWrapper);

        System.out.println("total == "+ iPage.getTotal());

        return Result.success(iPage.getTotal(),iPage.getRecords());
    }
    //获取数据
    @GetMapping("/list")
    public Result list(){
        List<Goodstype> list = goodstypeService.list();
        return list.isEmpty() ? Result.fail() : Result.success(list);
    }
}
