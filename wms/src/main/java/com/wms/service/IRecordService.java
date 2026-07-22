package com.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.dto.InGoodsRequest;
import com.wms.dto.RecordQuery;
import com.wms.dto.RecordRequest;
import com.wms.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nobody
 * @since 2026-07-06
 */
public interface IRecordService extends IService<Record> {

    IPage<RecordRequest> pageRecord(RecordQuery query);

    boolean createInventoryRecord(InGoodsRequest request);
}
