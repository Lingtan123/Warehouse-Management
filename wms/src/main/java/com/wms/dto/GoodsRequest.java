package com.wms.dto;

import lombok.Data;

@Data
public class GoodsRequest {
    private Integer id;
    private String name;
    private Integer storage;
    private Integer goodstype;
    private Integer count;
    private String remake;
}
