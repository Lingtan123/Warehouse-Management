package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wms.common.GoodsSaveRequest;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.mapper.GoodsMapper;
import com.wms.mapper.RecordMapper;
import com.wms.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nobody
 * @since 2026-07-06
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    private RecordMapper recordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithInitialRecord(GoodsSaveRequest request) {
        Goods goods = new Goods();
        goods.setName(request.getName());
        goods.setStorage(request.getStorage());
        goods.setGoodstype(request.getGoodstype());
        goods.setCount(request.getCount());
        goods.setRemake(request.getRemake());

        if (!save(goods)) {
            return false;
        }
        //同步生成订单记录
        Record record = buildRecord(goods.getId(), request, request.getCount());
        return recordMapper.insert(record) > 0;
    }

    @Override
    public boolean removeWithRecordCheck(Integer id) {
        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Record::getGoods, id);
        Long recordCount = recordMapper.selectCount(wrapper);
        if (recordCount != null && recordCount > 0) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public boolean hasEnoughStock(GoodsSaveRequest request) {
        Goods goods = getById(request.getId());
        return goods != null && goods.getCount() != null && goods.getCount() >= request.getCount();
    }

    private Record buildRecord(Integer goodsId, GoodsSaveRequest request, Integer count) {
        Record record = new Record();
        record.setGoods(goodsId);
        record.setUserId(request.getUserId());
        record.setAdminId(request.getAdminId());
        record.setCount(count);
        record.setRemake(request.getRemake());
        return record;
    }
}
