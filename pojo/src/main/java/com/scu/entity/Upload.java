package com.scu.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("upload")
public class Upload implements Serializable {

    @TableId
    private Integer id;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private BigDecimal ncOrder;

    private String attachment;

}
