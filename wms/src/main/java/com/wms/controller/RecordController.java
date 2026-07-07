package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.GoodsSaveRequest;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.service.IGoodsService;
import com.wms.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.wms.entity.Record;

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
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private IRecordService recordService;
    @Autowired
    private IGoodsService goodsService;

    @GetMapping("/delete")
    public Result delete(int id){
        return recordService.removeById(id) ? Result.success() : Result.fail();
    }

    @PostMapping("/list")
    public Result list(@RequestBody QueryPageParam query){
        System.out.println(query);
        HashMap map = query.getParam();
        Page<Record> page = new Page(query.getPageNum(),  query.getPageSize());
        String name = (String) map.get("name");
        String type = (String) map.get("goodstype");
        String storage = (String) map.get("storage");
        String roleId = (String) map.get("roleId");
        String userId = (String) map.get("userId");

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();

        queryWrapper.apply(" r.goods = g.id and g.storage = s.id and g.goodstype = gt.id ");

        if("2".equals(roleId)){
            queryWrapper.lambda().eq(Record::getUserId,userId);
        }

        if(StringUtils.isNotEmpty(name)){
            queryWrapper.like("g.name",name);
        }
        if(StringUtils.isNotEmpty(type)){
            queryWrapper.eq("gt.id",type);
        }
        if(StringUtils.isNotEmpty(storage)){
            queryWrapper.eq("s.id",storage);
        }

        IPage iPage = recordService.pageCC(page, queryWrapper);
        System.out.println("total ==" + iPage.getTotal());

        return Result.success(iPage.getTotal(),iPage.getRecords());
    }

    @PostMapping("/save")
    public Result save(@RequestBody GoodsSaveRequest request){
        Goods goods = goodsService.getById(request.getId());
        Record record = new Record();
        record.setGoods(goods.getId());
        record.setUserId(request.getUserId());
        record.setAdminId(request.getAdminId());
        record.setCount(request.getCount());
        record.setRemake(request.getRemake());
        int n = request.getCount();
        //出库
        if("2".equals(request.getAction())){
            n = -n;
            record.setCount(n);
        }
        int num = goods.getCount() + n;
        goods.setCount(num);
        goodsService.updateById(goods);
        return recordService.save(record) ? Result.success() : Result.fail();
    }
}
