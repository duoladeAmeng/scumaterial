package com.scu.enu;

import com.scu.constant.SqlTypeConstant;
//模板字段枚举
public enum FieldDataTypeEnum {
    STRING(0, "字符串型",SqlTypeConstant.VARCHAR_100),
    INTEGER(1,"数值型", SqlTypeConstant.INT),
    FLOAT(2,"浮点型", SqlTypeConstant.FLOAT),
    RANGE(3,"范围型",SqlTypeConstant.VARCHAR_100),
    Enumeration(4,"枚举型",SqlTypeConstant.VARCHAR_100),
    PICTURE(5,"图片型",SqlTypeConstant.VARCHAR_100),
    FILE(6,"文件型",SqlTypeConstant.VARCHAR_100),
    ARRAY(7,"数组型",SqlTypeConstant.VARCHAR_100),
    TABLE(8,"表格型",SqlTypeConstant.VARCHAR_100),
    DATE(9,"日期型",SqlTypeConstant.DATE);

    //类型编号
    private Integer code;
    //类型中文名称
    private String name;
    //对应mysql的类型名称
    private String sqlType;
    FieldDataTypeEnum(Integer code, String name,String sqlType) {
        this.code = code;
        this.name = name;
        this.sqlType = sqlType;
    }

    public static FieldDataTypeEnum getByName(String  name) {
        for (FieldDataTypeEnum value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public String getSqlType() {
        return sqlType;
    }

    public static FieldDataTypeEnum getByCode(Integer code) {
        if (code == null) return null;
        for (FieldDataTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
