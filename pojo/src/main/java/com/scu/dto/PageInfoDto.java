package com.scu.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageInfoDto {
    private Integer pageNum;
    private Integer pageSize;
}
