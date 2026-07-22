package com.wms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordRequest {

    private Integer id;
    private Integer goods;
    private Integer userId;
    private Integer adminId;
    private Integer count;
    private LocalDateTime time;
    private String remake;

    private String userName;
    private String adminName;
    private String goodsName;
    private String storageName;
    private String goodstypeName;
}
