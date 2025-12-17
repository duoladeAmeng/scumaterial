package com.scu.entity;


import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "模板类别")
public class TemplateCategory {
    @Schema(description="分类主键")
    @TableId
    private Integer id;
    @Schema(description="分类名称")
    private String categoryName;
    @Schema(description="该分类的上级分类，-1表示顶级类别")
    private Integer supLevel;
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
