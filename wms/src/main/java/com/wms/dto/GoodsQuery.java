package com.wms.dto;

import lombok.Data;

@Data
public class GoodsQuery extends QueryPageParam {
    private String name;
    private String storage;
    private String goodstype;
}
