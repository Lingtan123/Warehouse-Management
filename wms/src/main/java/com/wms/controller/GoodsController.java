package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author nobody
 * @since 2026-07-06
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IGoodsService goodsService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        return goodsService.save(goods) ? Result.success() : Result.fail();
    }
    //修改
    @PostMapping("/mod")
    public Result mod(@RequestBody Goods goods){
        return goodsService.updateById(goods) ? Result.success() : Result.fail();
    }
    //删除
    @GetMapping("/delete")
    public Result delete(int id){
        return goodsService.removeById(id) ? Result.success() : Result.fail();
    }
    //分页查询（后端返回数据封装）（和自定义分页使用同一份返回）
    //使用自定义类Result封装
    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam query){
        System.out.println(query);
        HashMap param = query.getParam();
        Page<Goods> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name =(String) param.get("name");
        String storage =(String) param.get("storage");
        String goodstype =(String) param.get("goodstype");

        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();

        if(StringUtils.isNotBlank(name)){
            queryWrapper.like(Goods::getName,name);
        }
        if(StringUtils.isNotBlank(storage)){
            queryWrapper.eq(Goods::getStorage,storage);
        }
        if(StringUtils.isNotBlank(goodstype)){
            queryWrapper.eq(Goods::getGoodstype,goodstype);
        }

        IPage iPage = goodsService.page(page, queryWrapper);

        System.out.println("total == "+ iPage.getTotal());

        return Result.success(iPage.getTotal(),iPage.getRecords());
    }
}
