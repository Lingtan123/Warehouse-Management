package com.wms.common;

import com.wms.entity.User;
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

    public static Result unauthorized(String msg) {
        return new Result(401, msg, 0L, null);
    }

    public static Result success() {
        return new Result(200, "成功", 0L, null);
    }

    public static Result success(List<User> list) {
        int sum = 0;
        for (User user : list) {
            sum++;
        }
        return new Result(200, "成功", sum, list);
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
