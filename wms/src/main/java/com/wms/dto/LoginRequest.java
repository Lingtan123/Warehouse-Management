package com.wms.dto;

import lombok.Data;

//前端请求登录参数封装类
@Data
public class LoginRequest {
    String no;
    String password;
}
