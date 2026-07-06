package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.entity.Record;
import com.wms.mapper.RecordMapper;
import com.wms.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
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
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {
    @Resource
    private RecordMapper recordMapper;
    @Override
    public IPage<Record> pageCC(IPage<Record> page, Wrapper<Record> wrapper) {
        return recordMapper.pageCC(page,wrapper);
    }
}
