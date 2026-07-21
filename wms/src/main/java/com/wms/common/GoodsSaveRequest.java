package com.wms.common;

import com.wms.entity.Goods;
import lombok.Data;

//接收前端请求参数封装类
@Data
public class GoodsSaveRequest extends Goods {
    private Integer userId;
    private String userName;
    private Integer adminId;
    private String adminName;
    private String action;
}
