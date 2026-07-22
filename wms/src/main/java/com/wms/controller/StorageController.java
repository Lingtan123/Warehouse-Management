package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.auth.CurrentUser;
import com.wms.auth.UserContext;
import com.wms.dto.StorageQuery;
import com.wms.common.Result;
import com.wms.dto.StorageRequest;
import com.wms.entity.Storage;
import com.wms.exception.AllException;
import com.wms.exception.myException;
import com.wms.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private IStorageService storageService;

    @PostMapping("/save")
    public Result save(@RequestBody StorageRequest storage) {
        if (!storageService.createStorage(storage)) {
            throw new myException(AllException.STORAGE_INSERT_ERROR);
        }
        log.info("event=CREATE_STORAGE operatorId={} operatorNo={} operatorName={} storageId={} storageName={}",
                operatorId(), operatorNo(), operatorName(), storage.getId(), storage.getName());
        return Result.success();
    }

    @PostMapping("/mod")
    public Result mod(@RequestBody StorageRequest storage) {
        if (!storageService.updateStorage(storage)) {
            throw new myException(AllException.STORAGE_UPDATE_ERROR);
        }
        log.info("event=UPDATE_STORAGE operatorId={} operatorNo={} operatorName={} storageId={} storageName={}",
                operatorId(), operatorNo(), operatorName(), storage.getId(), storage.getName());
        return Result.success();
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        Storage storage = storageService.getById(id);
        if (!storageService.removeById(id)) {
            throw new myException(AllException.STORAGE_DELETE_ERROR);
        }
        log.info("event=DELETE_STORAGE operatorId={} operatorNo={} operatorName={} storageId={} storageName={}",
                operatorId(), operatorNo(), operatorName(),
                storage == null ? id : storage.getId(),
                storage == null ? null : storage.getName());
        return Result.success();
    }

    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody StorageQuery query) {
        IPage<Storage> iPage = storageService.listPageC(query);
        return Result.success(iPage.getTotal(), iPage.getRecords());
    }

    @GetMapping("/list")
    public Result list() {
        List<Storage> list = storageService.list();
        return list.isEmpty() ? Result.fail() : Result.success(list);
    }

    private Integer operatorId() {
        CurrentUser currentUser = UserContext.getCurrentUser();
        return currentUser == null ? null : currentUser.getId();
    }

    private String operatorNo() {
        CurrentUser currentUser = UserContext.getCurrentUser();
        return currentUser == null ? null : currentUser.getNo();
    }

    private String operatorName() {
        CurrentUser currentUser = UserContext.getCurrentUser();
        return currentUser == null ? null : currentUser.getName();
    }
}
