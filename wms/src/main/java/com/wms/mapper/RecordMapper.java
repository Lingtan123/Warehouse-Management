package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.dto.RecordRequest;
import com.wms.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author nobody
 * @since 2026-07-06
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
    IPage<RecordRequest> pageCC(IPage<RecordRequest> page, @Param(Constants.WRAPPER) Wrapper<Record> wrapper);
}
