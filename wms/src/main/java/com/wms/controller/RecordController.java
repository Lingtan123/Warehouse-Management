package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.common.GoodsSaveRequest;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
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
        return recordService.removeById(id) ? Result.success() : Result.fail();
    }

    @PostMapping("/list")
    public Result list(@RequestBody QueryPageParam query) {
        IPage<?> iPage = recordService.pageRecord(query);
        return Result.success(iPage.getTotal(), iPage.getRecords());
    }

    @PostMapping("/save")
    public Result save(@RequestBody GoodsSaveRequest request) {
        return recordService.saveInventoryRecord(request) ? Result.success() : Result.fail();
    }
}
