package com.wms.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//封装当前操作人员信息，便于日志记录管理
public class CurrentUser {
    private Integer id;
    private String no;
    private String name;
    private Integer roleId;
}
