package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.dto.GoodstypeQuery;
import com.wms.dto.GoodstypeRequest;
import com.wms.entity.Goodstype;
import com.wms.mapper.GoodstypeMapper;
import com.wms.service.IGoodstypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nobody
 * @since 2026-07-06
 */
@Service
public class GoodstypeServiceImpl extends ServiceImpl<GoodstypeMapper, Goodstype> implements IGoodstypeService {

    @Override
    public boolean createGoodstype(GoodstypeRequest request) {
        Goodstype goodstype = new Goodstype();
        BeanUtils.copyProperties(request,goodstype);
        boolean saved = save(goodstype);
        if (saved) {
            request.setId(goodstype.getId());
        }
        return saved;
    }

    @Override
    public boolean updateGoodstype(GoodstypeRequest request) {
        Goodstype goodstype = new Goodstype();
        BeanUtils.copyProperties(request,goodstype);
        return updateById(goodstype);
    }

    @Override
    public IPage<Goodstype> listPageC(GoodstypeQuery query) {
        Page<Goodstype> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Goodstype> queryWrapper = new LambdaQueryWrapper<>();
        String name = query.getName();

        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Goodstype::getName, name);
        }

        return this.page(page, queryWrapper);
    }
}
