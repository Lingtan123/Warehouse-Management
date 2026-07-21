package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.GoodsSaveRequest;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @PostMapping("/save")
    public Result save(@RequestBody GoodsSaveRequest request) {
        return goodsService.saveWithInitialRecord(request) ? Result.success() : Result.fail();
    }

    @PostMapping("/mod")
    public Result mod(@RequestBody Goods goods) {
        return goodsService.updateById(goods) ? Result.success() : Result.fail();
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        return goodsService.removeWithRecordCheck(id) ? Result.success() : Result.fail();
    }

    @PostMapping("/getCount")
    public Result getCount(@RequestBody GoodsSaveRequest request) {
        return goodsService.hasEnoughStock(request) ? Result.success() : Result.fail();
    }

    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        Page<Goods> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name = (String) param.get("name");
        String storage = (String) param.get("storage");
        String goodstype = (String) param.get("goodstype");

        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Goods::getName, name);
        }
        if (StringUtils.isNotBlank(storage)) {
            queryWrapper.eq(Goods::getStorage, storage);
        }
        if (StringUtils.isNotBlank(goodstype)) {
            queryWrapper.eq(Goods::getGoodstype, goodstype);
        }

        IPage<Goods> iPage = goodsService.page(page, queryWrapper);
        return Result.success(iPage.getTotal(), iPage.getRecords());
    }
}
