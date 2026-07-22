package com.wms.dto;

import lombok.Data;

@Data
public class RecordQuery extends QueryPageParam {
    private String name;
    private String goodstype;
    private String storage;
    private String roleId;
    private String userId;
}
