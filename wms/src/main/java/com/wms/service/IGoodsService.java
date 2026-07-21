package com.wms.service;

import com.wms.common.GoodsSaveRequest;
import com.wms.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nobody
 * @since 2026-07-06
 */
public interface IGoodsService extends IService<Goods> {

    boolean saveWithInitialRecord(GoodsSaveRequest request);

    boolean removeWithRecordCheck(Integer id);

    boolean hasEnoughStock(GoodsSaveRequest request);
}
