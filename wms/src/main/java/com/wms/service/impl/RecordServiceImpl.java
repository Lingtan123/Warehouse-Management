package com.wms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.GoodsSaveRequest;
import com.wms.common.QueryPageParam;
import com.wms.common.RecordResult;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.mapper.GoodsMapper;
import com.wms.mapper.RecordMapper;
import com.wms.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nobody
 * @since 2026-07-06
 */
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
            return false;
        }

        //处理出库，转换新增为负数，剩余不足就失败
        int delta = resolveDelta(request);
        int targetCount = goods.getCount() + delta;
        if (targetCount < 0) {
            return false;
        }

        goods.setCount(targetCount);
        if (goodsMapper.updateById(goods) <= 0) {
            return false;
        }

        Record record = new Record();
        record.setGoods(goods.getId());
        record.setUserId(request.getUserId());
        record.setAdminId(request.getAdminId());
        record.setCount(delta);
        record.setRemake(request.getRemake());
        return recordMapper.insert(record) > 0;
    }

    private int resolveDelta(GoodsSaveRequest request) {
        int count = request.getCount();
        if ("2".equals(request.getAction())) {
            return -count;
        }
        return count;
    }
}
