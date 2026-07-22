package com.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.dto.StorageQuery;
import com.wms.dto.StorageRequest;
import com.wms.entity.Storage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nobody
 * @since 2026-07-06
 */
public interface IStorageService extends IService<Storage> {

    boolean createStorage(StorageRequest request);

    boolean updateStorage(StorageRequest request);

    IPage<Storage> listPageC(StorageQuery query);
}
