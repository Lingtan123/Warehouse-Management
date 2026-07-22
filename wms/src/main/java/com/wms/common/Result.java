package com.wms.common;

import lombok.Data;

import java.util.List;

@Data
public class Result {
    private int code;
    private String msg;
    private long total;
    private Object data;

    public static Result fail() {
        return new Result(400, "失败", 0L, null);
    }

    public static Result fail(String msg) {
        return new Result(400, msg, 0L, null);
    }

    public static Result unauthorized(String msg) {
        return new Result(401, msg, 0L, null);
    }

    public static Result success() {
        return new Result(200, "成功", 0L, null);
    }

    public static Result success(List<?> list) {
        return new Result(200, "成功", list == null ? 0L : list.size(), list);
    }

    public static Result success(Object data) {
        return new Result(200, "成功", 0L, data);
    }

    public static Result success(Long total, Object data) {
        return new Result(200, "成功", total, data);
    }

    public Result(int code, String msg, long total, Object data) {
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.data = data;
    }
}
