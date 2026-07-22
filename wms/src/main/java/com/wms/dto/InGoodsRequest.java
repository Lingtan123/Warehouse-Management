package com.wms.dto;

import lombok.Data;

//前端请求商品新增、入库、出库参数封装类
@Data
public class InGoodsRequest {
    private Integer id;
    private String name;
    private Integer storage;
    private Integer goodstype;
    private Integer count;
    private String remake;
    private Integer userId;
    private String userName;
    private Integer adminId;
    private String adminName;
    private String action;
}
