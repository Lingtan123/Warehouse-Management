package com.wms.dto;

import lombok.Data;

@Data
public class UserQuery extends QueryPageParam{
    private String name;
    private String sex;
    private String roleId;
}
