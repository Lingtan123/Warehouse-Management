package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.auth.CurrentUser;
import com.wms.auth.UserContext;
import com.wms.dto.GoodsQuery;
import com.wms.dto.GoodsRequest;
import com.wms.dto.InGoodsRequest;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.exception.AllException;
import com.wms.exception.myException;
import com.wms.mapper.GoodsMapper;
import com.wms.mapper.RecordMapper;
import com.wms.service.IGoodsService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    private RecordMapper recordMapper;

    @Override
    public boolean updateGoods(GoodsRequest request){
        Goods good =  new Goods();
        BeanUtils.copyProperties(request,good);
        return updateById(good);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrInGoods(InGoodsRequest request) {
        Goods goods = new Goods();
        goods.setName(request.getName());
        goods.setStorage(request.getStorage());
        goods.setGoodstype(request.getGoodstype());
        goods.setCount(request.getCount());
        goods.setRemake(request.getRemake());

        if (!save(goods)) {
            throw new myException(AllException.GOODS_INSERT_ERROR);
        }

        request.setId(goods.getId());
        CurrentUser currentUser = UserContext.getCurrentUser();
        if (currentUser != null) {
            request.setAdminId(currentUser.getId());
            request.setAdminName(currentUser.getName());
        }

        Record record = buildRecord(goods.getId(), request, request.getCount());

        if (recordMapper.insert(record) <= 0) {
            throw new myException(AllException.RECORD_INSERT_ERROR);
        }
        return true;
    }

    @Override
    public boolean removeWithRecordCheck(Integer id) {
        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Record::getGoods, id);
        Long recordCount = recordMapper.selectCount(wrapper);
        if (recordCount != null && recordCount > 0) {
            throw new myException(AllException.GOODS_DELETE_ERROR);
        }
        if (!removeById(id)) {
            throw new myException(AllException.GOODS_DELETE_ERROR);
        }
        return true;
    }

    @Override
    public boolean checkEnoughStock(InGoodsRequest request) {
        Goods goods = getById(request.getId());
        return goods != null && goods.getCount() != null && goods.getCount() >= request.getCount();
    }

    @Override
    public IPage<Goods> listPageC(GoodsQuery query) {
        Page<Goods> page = new Page<>(query.getPageNum(), query.getPageSize());

        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        String name = query.getName();
        String storage = query.getStorage();
        String goodstype = query.getGoodstype();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Goods::getName, name);
        }
        if (StringUtils.isNotBlank(storage)) {
            queryWrapper.eq(Goods::getStorage, storage);
        }
        if (StringUtils.isNotBlank(goodstype)) {
            queryWrapper.eq(Goods::getGoodstype, goodstype);
        }
        IPage<Goods> iPage = this.page(page, queryWrapper);
        return iPage;
    }

    private Record buildRecord(Integer goodsId, InGoodsRequest request, Integer count) {
        Record record = new Record();
        record.setGoods(goodsId);
        record.setUserId(request.getUserId());
        record.setAdminId(request.getAdminId());
        record.setCount(count);
        record.setRemake(request.getRemake());
        return record;
    }
}
