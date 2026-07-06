package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.wms.entity.Record;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/list")
    public Result list(@RequestBody QueryPageParam query){
        System.out.println(query);
        HashMap map = query.getParam();
        Page<Record> page = new Page(query.getPageNum(),  query.getPageSize());
        String name = (String) map.get("name");
        String type = (String) map.get("goodstype");
        String storage = (String) map.get("storage");

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();

        queryWrapper.apply(" r.goods = g.id and g.storage = s.id and g.goodstype = gt.id ");
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
}
