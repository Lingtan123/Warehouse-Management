package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.auth.CurrentUser;
import com.wms.auth.UserContext;
import com.wms.common.GoodsSaveRequest;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.exception.AllException;
import com.wms.exception.myException;
import com.wms.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @PostMapping("/save")
    public Result save(@RequestBody GoodsSaveRequest request) {
        goodsService.saveWithInitialRecord(request);
        log.info("event=CREATE_GOODS operatorId={} operatorNo={} operatorName={} goodsId={} goodsName={} storageId={} goodstypeId={} initialCount={} executorUserId={} executorUserName={}",
                operatorId(), operatorNo(), operatorName(),
                request.getId(), request.getName(), request.getStorage(), request.getGoodstype(), request.getCount(),
                request.getUserId(), request.getUserName());
        return Result.success();
    }

    @PostMapping("/mod")
    public Result mod(@RequestBody Goods goods) {
        if (!goodsService.updateById(goods)) {
            throw new myException(AllException.GOODS_UPDATE_ERROR);
        }
        log.info("event=UPDATE_GOODS operatorId={} operatorNo={} operatorName={} goodsId={} goodsName={} storageId={} goodstypeId={} count={}",
                operatorId(), operatorNo(), operatorName(),
                goods.getId(), goods.getName(), goods.getStorage(), goods.getGoodstype(), goods.getCount());
        return Result.success();
    }

    @GetMapping("/delete")
    public Result delete(int id) {
        Goods goods = goodsService.getById(id);
        goodsService.removeWithRecordCheck(id);
        log.info("event=DELETE_GOODS operatorId={} operatorNo={} operatorName={} goodsId={} goodsName={}",
                operatorId(), operatorNo(), operatorName(),
                goods == null ? id : goods.getId(), goods == null ? null : goods.getName());
        return Result.success();
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
