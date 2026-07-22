package com.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.dto.GoodsQuery;
import com.wms.dto.GoodsRequest;
import com.wms.dto.InGoodsRequest;
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

    boolean createOrInGoods(InGoodsRequest request);

    boolean removeWithRecordCheck(Integer id);

    boolean checkEnoughStock(InGoodsRequest request);

    boolean updateGoods(GoodsRequest goods);

    IPage<Goods> listPageC(GoodsQuery query);
}
