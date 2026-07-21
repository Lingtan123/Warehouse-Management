package com.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.common.GoodsSaveRequest;
import com.wms.common.QueryPageParam;
import com.wms.common.RecordResult;
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

    IPage<RecordResult> pageRecord(QueryPageParam query);

    boolean saveInventoryRecord(GoodsSaveRequest request);
}
