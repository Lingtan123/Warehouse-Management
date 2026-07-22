package com.wms.exception;

public enum AllException {
    NO_ERROR("没有错误"),
    LOGIN_ERROR("用户名或密码错误"),
    GOODS_UPDATE_ERROR("库存更新失败"),
    GOODS_DEFICIENCY("库存不足"),
    GOODS_INSERT_ERROR("商品新增失败"),
    GOODS_DELETE_ERROR("商品删除失败"),
    RECORD_INSERT_ERROR("订单记录插入失败"),
    RECORD_DELETE_ERROR("订单记录删除失败"),
    NO_THIS_GOOD("商品信息不存在"),
    TOKEN_ERROR("令牌异常"),
    TOKEN_EXPIRED("令牌过期"),
    USER_INSERT_ERROR("员工新增失败"),
    USER_UPDATE_ERROR("员工修改失败"),
    USER_DELETE_ERROR("员工删除失败"),
    STORAGE_INSERT_ERROR("仓库新增失败"),
    STORAGE_UPDATE_ERROR("仓库修改失败"),
    STORAGE_DELETE_ERROR("仓库删除失败"),
    GOODSTYPE_INSERT_ERROR("分类新增失败"),
    GOODSTYPE_UPDATE_ERROR("分类修改失败"),
    GOODSTYPE_DELETE_ERROR("分类删除失败");

    private final String error;

    AllException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
