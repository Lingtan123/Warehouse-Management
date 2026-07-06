package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    IPage<Record> pageCC(IPage<Record> page, Wrapper<Record> wrapper);
}
