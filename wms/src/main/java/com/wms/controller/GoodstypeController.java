package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.auth.CurrentUser;
import com.wms.auth.UserContext;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goodstype;
import com.wms.service.IGoodstypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private IGoodstypeService goodstypeService;

    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype) {
        boolean saved = goodstypeService.save(goodstype);
        if (saved) {
            log.info("event=CREATE_GOODSTYPE operatorId={} operatorNo={} operatorName={} goodstypeId={} goodstypeName={}",
                    operatorId(), operatorNo(), operatorName(), goodstype.getId(), goodstype.getName());
            return Result.success();
        }

        log.warn("event=CREATE_GOODSTYPE_FAIL operatorId={} operatorNo={} operatorName={} goodstypeName={}",
                operatorId(), operatorNo(), operatorName(), goodstype.getName());
        return Result.fail();
    }

    @PostMapping("/mod")
    public Result mod(@RequestBody Goodstype goodstype) {
        boolean updated = goodstypeService.updateById(goodstype);
        if (updated) {
            log.info("event=UPDATE_GOODSTYPE operatorId={} operatorNo={} operatorName={} goodstypeId={} goodstypeName={}",
                    operatorId(), operatorNo(), operatorName(), goodstype.getId(), goodstype.getName());
            return Result.success();
        }

        log.warn("event=UPDATE_GOODSTYPE_FAIL operatorId={} operatorNo={} operatorName={} goodstypeId={} goodstypeName={}",
                operatorId(), operatorNo(), operatorName(), goodstype.getId(), goodstype.getName());
        return Result.fail();
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        Goodstype goodstype = goodstypeService.getById(id);
        boolean removed = goodstypeService.removeById(id);
        if (removed) {
            log.info("event=DELETE_GOODSTYPE operatorId={} operatorNo={} operatorName={} goodstypeId={} goodstypeName={}",
                    operatorId(), operatorNo(), operatorName(),
                    goodstype == null ? id : goodstype.getId(),
                    goodstype == null ? null : goodstype.getName());
            return Result.success();
        }

        log.warn("event=DELETE_GOODSTYPE_FAIL operatorId={} operatorNo={} operatorName={} goodstypeId={} goodstypeName={}",
                operatorId(), operatorNo(), operatorName(),
                goodstype == null ? id : goodstype.getId(),
                goodstype == null ? null : goodstype.getName());
        return Result.fail();
    }

    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        Page<Goodstype> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name = (String) param.get("name");

        LambdaQueryWrapper<Goodstype> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Goodstype::getName, name);
        }

        IPage<Goodstype> iPage = goodstypeService.page(page, queryWrapper);
        return Result.success(iPage.getTotal(), iPage.getRecords());
    }

    @GetMapping("/list")
    public Result list() {
        List<Goodstype> list = goodstypeService.list();
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
