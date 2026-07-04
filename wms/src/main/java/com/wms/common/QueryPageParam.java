package com.wms.common;

import lombok.Data;

import java.util.HashMap;
//封装请求对象，也可以不进行封装直接使用Hashmap，但是封装可以更便于进行阅读代码
@Data
public class QueryPageParam {
    //默认值
    private static int PAGE_SIZE = 20;
    private static int PAGE_NUM = 1;

    private int pageSize=PAGE_SIZE;
    private int pageNum=PAGE_NUM;

    private HashMap param = new HashMap();
}
