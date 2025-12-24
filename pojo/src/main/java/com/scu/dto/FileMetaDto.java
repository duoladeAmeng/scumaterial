package com.scu.dto;

import lombok.Data;

@Data

public class FileMetaDto {
    private String key;   // 业务含义，在本项目中，如果是单条添加方式，则为字段名；如果是批量方式，则为路径名
    private int index;    // files 中的下标
}