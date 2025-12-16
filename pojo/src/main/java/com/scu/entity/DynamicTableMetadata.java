package com.scu.entity;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "动态表的元数据信息")
public class DynamicTableMetadata {
    @Schema(description="id")
    @TableId
    private Long id;
    @Schema(description="对应模板id")
    private Long templateId;
    @Schema(description="")
    private Date createTime;
    @Schema(description="")
    private Date updateTime;
    @Schema(description="表名")
    private String tableName;
    @Schema(description="描述")
    private String description;
}
