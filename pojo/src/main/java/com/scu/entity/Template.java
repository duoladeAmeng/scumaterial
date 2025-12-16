package com.scu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@TableName("template")
@Data
@Schema(description = "模板实体类")
public class Template {
    @Schema(description = "模板id")
    @TableId
    Long id;

    @Schema(description = "模板名")
    String name;

    @Schema(description = "模板描述")
    String description;

    @Schema(description = "模板创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createTime;

    @Schema(description = "最近一次模板更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime;

    @Schema(description = "模板创建者")
    String creator;

    @Schema(description = "模板所属的类别")
    int categoryId;

    @Schema(description = "模板的审核状态（0：未审核，1：已审核）")
    int state;
}
