package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.dto.InGoodsRequest;
import com.wms.dto.RecordQuery;
import com.wms.common.Result;
import com.wms.exception.AllException;
import com.wms.exception.myException;
import com.wms.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private IRecordService recordService;

    @GetMapping("/delete")
    public Result delete(int id) {
        if (!recordService.removeById(id)) {
            throw new myException(AllException.RECORD_DELETE_ERROR);
        }
        return Result.success();
    }

    @PostMapping("/list")
    public Result list(@RequestBody RecordQuery query) {
        IPage<?> iPage = recordService.pageRecord(query);
        return Result.success(iPage.getTotal(), iPage.getRecords());
    }

    @PostMapping("/save")
    public Result save(@RequestBody InGoodsRequest request) {
        recordService.createInventoryRecord(request);
        return Result.success();
    }
}
