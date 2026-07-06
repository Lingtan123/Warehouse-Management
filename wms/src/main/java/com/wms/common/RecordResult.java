package com.wms.common;

import com.wms.entity.Record;
import lombok.Data;

@Data
public class RecordResult extends Record {
    private String userName;
    private String adminName;
    private String goodsName;
    private String storageName;
    private String goodstypeName;
}
