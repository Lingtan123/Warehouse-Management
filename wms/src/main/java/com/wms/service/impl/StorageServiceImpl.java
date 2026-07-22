package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.dto.StorageQuery;
import com.wms.dto.StorageRequest;
import com.wms.entity.Storage;
import com.wms.mapper.StorageMapper;
import com.wms.service.IStorageService;
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
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements IStorageService {

    @Override
    public boolean createStorage(StorageRequest request) {
        Storage storage = new Storage();
        BeanUtils.copyProperties(request,storage);
        boolean saved = save(storage);
        if (saved) {
            request.setId(storage.getId());
        }
        return saved;
    }

    @Override
    public boolean updateStorage(StorageRequest request) {
        Storage storage = new Storage();
        BeanUtils.copyProperties(request,storage);
        return updateById(storage);
    }

    @Override
    public IPage<Storage> listPageC(StorageQuery query) {
        Page<Storage> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Storage> queryWrapper = new LambdaQueryWrapper<>();
        String name = query.getName();

        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Storage::getName, name);
        }

        return this.page(page, queryWrapper);
    }
}
