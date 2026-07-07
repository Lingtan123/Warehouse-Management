package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.GoodsSaveRequest;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.IGoodsService;
import com.wms.service.IRecordService;
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
    @Autowired
    private IRecordService recordService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody GoodsSaveRequest request){
        Goods goods = new Goods();
        goods.setName(request.getName());
        goods.setStorage(request.getStorage());
        goods.setGoodstype(request.getGoodstype());
        goods.setCount(request.getCount());
        goods.setRemake(request.getRemake());
        boolean flag1 = goodsService.save(goods);
        if(!flag1){
            return Result.fail();
        }
        Record record = new Record();
        record.setGoods(goods.getId());
        record.setUserId(request.getUserId());
        record.setAdminId(request.getAdminId());
        record.setCount(request.getCount());
        record.setRemake(request.getRemake());
        boolean flag2 = recordService.save(record);

        return flag2 ? Result.success() : Result.fail();
    }
    //修改
    @PostMapping("/mod")
    public Result mod(@RequestBody Goods goods){
        return goodsService.updateById(goods) ? Result.success() : Result.fail();
    }
    //删除
    @GetMapping("/delete")
    public Result delete(int id){
        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Record::getGoods, id);
        long count = recordService.count(wrapper);
        if (count > 0) {
            return Result.fail();
        }
        return goodsService.removeById(id) ? Result.success() : Result.fail();
    }
    //检查对应物品数量是否足够
    @PostMapping("/getCount")
    public Result getCount(@RequestBody GoodsSaveRequest request){
        Goods goods = goodsService.getById(request.getId());
        if (goods == null) {
            return Result.fail();
        }
        if (goods.getCount() < request.getCount()) {
            return Result.fail();
        }
        return Result.success();
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
