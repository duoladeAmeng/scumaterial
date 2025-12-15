package com.scu.entity;



import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("module_conflict_table")
public class ModuleLink {

    @TableId
    private int id;

    private int moduleId;

    private String objectTableName;

    private String operationTableName;

    private String resultTableName;
}
