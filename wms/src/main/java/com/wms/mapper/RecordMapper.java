package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.naming.Context;

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
    IPage<Record> pageCC(IPage<Record> page,@Param(Constants.WRAPPER) Wrapper<Record> wrapper);
}
