package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.auth.CurrentUser;
import com.wms.auth.UserContext;
import com.wms.common.GoodsSaveRequest;
import com.wms.common.QueryPageParam;
import com.wms.common.RecordResult;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.exception.AllException;
import com.wms.exception.myException;
import com.wms.mapper.GoodsMapper;
import com.wms.mapper.RecordMapper;
import com.wms.service.IRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Slf4j
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {
    @Resource
    private RecordMapper recordMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public IPage<RecordResult> pageRecord(QueryPageParam query) {
        HashMap param = query.getParam();
        Page<RecordResult> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name = (String) param.get("name");
        String type = (String) param.get("goodstype");
        String storage = (String) param.get("storage");
        String roleId = (String) param.get("roleId");
        String userId = (String) param.get("userId");

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(" r.goods = g.id and g.storage = s.id and g.goodstype = gt.id ");

        if ("2".equals(roleId)) {
            queryWrapper.lambda().eq(Record::getUserId, userId);
        }
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like("g.name", name);
        }
        if (StringUtils.isNotEmpty(type)) {
            queryWrapper.eq("gt.id", type);
        }
        if (StringUtils.isNotEmpty(storage)) {
            queryWrapper.eq("s.id", storage);
        }

        return recordMapper.pageCC(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveInventoryRecord(GoodsSaveRequest request) {
        Goods goods = goodsMapper.selectById(request.getId());
        if (goods == null || goods.getCount() == null) {
            log.warn("event=INVENTORY_OPERATE_FAIL operatorId={} operatorNo={} operatorName={} goodsId={} action={} reason=goods_not_found",
                    operatorId(), operatorNo(), operatorName(), request.getId(), actionName(request));
            throw new myException(AllException.NO_THIS_GOOD);
        }

        CurrentUser currentUser = UserContext.getCurrentUser();
        if (currentUser != null) {
            request.setAdminId(currentUser.getId());
            request.setAdminName(currentUser.getName());
        }

        int beforeCount = goods.getCount();
        int delta = resolveDelta(request);
        int targetCount = beforeCount + delta;
        if (targetCount < 0) {
            log.warn("event=OUT_STOCK_FAIL operatorId={} operatorNo={} operatorName={} executorUserId={} executorUserName={} goodsId={} goodsName={} requestCount={} currentCount={} reason=insufficient_stock",
                    operatorId(), operatorNo(), operatorName(),
                    request.getUserId(), request.getUserName(), goods.getId(), goods.getName(), request.getCount(), beforeCount);
            throw new myException(AllException.GOODS_DEFICIENCY);
        }

        goods.setCount(targetCount);
        if (goodsMapper.updateById(goods) <= 0) {
            log.warn("event=INVENTORY_OPERATE_FAIL operatorId={} operatorNo={} operatorName={} goodsId={} goodsName={} action={} reason=update_stock_failed",
                    operatorId(), operatorNo(), operatorName(), goods.getId(), goods.getName(), actionName(request));
            throw new myException(AllException.GOODS_UPDATE_ERROR);
        }

        Record record = new Record();
        record.setGoods(goods.getId());
        record.setUserId(request.getUserId());
        record.setAdminId(request.getAdminId());
        record.setCount(delta);
        record.setRemake(request.getRemake());
        if (recordMapper.insert(record) <= 0) {
            log.warn("event=INVENTORY_OPERATE_FAIL operatorId={} operatorNo={} operatorName={} goodsId={} goodsName={} action={} reason=insert_record_failed",
                    operatorId(), operatorNo(), operatorName(), goods.getId(), goods.getName(), actionName(request));
            throw new myException(AllException.RECORD_INSERT_ERROR);
        }

        log.info("event={} operatorId={} operatorNo={} operatorName={} executorUserId={} executorUserName={} goodsId={} goodsName={} count={} beforeCount={} afterCount={}",
                actionName(request), operatorId(), operatorNo(), operatorName(),
                request.getUserId(), request.getUserName(), goods.getId(), goods.getName(),
                request.getCount(), beforeCount, targetCount);
        return true;
    }

    private int resolveDelta(GoodsSaveRequest request) {
        int count = request.getCount();
        if ("2".equals(request.getAction())) {
            return -count;
        }
        return count;
    }

    private String actionName(GoodsSaveRequest request) {
        return "2".equals(request.getAction()) ? "OUT_STOCK" : "IN_STOCK";
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
