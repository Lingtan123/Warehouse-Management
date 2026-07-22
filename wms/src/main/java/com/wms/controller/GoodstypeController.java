package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.auth.CurrentUser;
import com.wms.auth.UserContext;
import com.wms.dto.GoodstypeQuery;
import com.wms.dto.GoodstypeRequest;
import com.wms.common.Result;
import com.wms.entity.Goodstype;
import com.wms.exception.AllException;
import com.wms.exception.myException;
import com.wms.service.IGoodstypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private IGoodstypeService goodstypeService;

    @PostMapping("/save")
    public Result save(@RequestBody GoodstypeRequest goodstype) {
        if (!goodstypeService.createGoodstype(goodstype)) {
            throw new myException(AllException.GOODSTYPE_INSERT_ERROR);
        }
        log.info("event=CREATE_GOODSTYPE operatorId={} operatorNo={} operatorName={} goodstypeId={} goodstypeName={}",
                operatorId(), operatorNo(), operatorName(), goodstype.getId(), goodstype.getName());
        return Result.success();
    }

    @PostMapping("/mod")
    public Result mod(@RequestBody GoodstypeRequest goodstype) {
        if (!goodstypeService.updateGoodstype(goodstype)) {
            throw new myException(AllException.GOODSTYPE_UPDATE_ERROR);
        }
        log.info("event=UPDATE_GOODSTYPE operatorId={} operatorNo={} operatorName={} goodstypeId={} goodstypeName={}",
                operatorId(), operatorNo(), operatorName(), goodstype.getId(), goodstype.getName());
        return Result.success();
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        Goodstype goodstype = goodstypeService.getById(id);
        if (!goodstypeService.removeById(id)) {
            throw new myException(AllException.GOODSTYPE_DELETE_ERROR);
        }
        log.info("event=DELETE_GOODSTYPE operatorId={} operatorNo={} operatorName={} goodstypeId={} goodstypeName={}",
                operatorId(), operatorNo(), operatorName(),
                goodstype == null ? id : goodstype.getId(),
                goodstype == null ? null : goodstype.getName());
        return Result.success();
    }

    @PostMapping("/listPageC")
    public Result listPageC(@RequestBody GoodstypeQuery query) {
        IPage<Goodstype> iPage = goodstypeService.listPageC(query);
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
