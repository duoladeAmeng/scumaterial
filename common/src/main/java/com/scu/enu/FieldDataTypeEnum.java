package com.scu.enu;

import com.scu.constant.SqlTypeConstant;
//模板字段枚举
public enum FieldDataTypeEnum {
    INTEGER(0, SqlTypeConstant.INT),
    BIGINTEGER(1, SqlTypeConstant.BIGINT),
    STRING(2, SqlTypeConstant.VARCHAR_100),
    TEXT(3, SqlTypeConstant.TEXT),
    Enumeration(4,SqlTypeConstant.INT);
//    INTEGER(2, SqlTypeConstant.INT),
//    FLOAT(3,SqlTypeConstant.FLOAT);
    //......
    private Integer code;
    private String sqlType;
    FieldDataTypeEnum(Integer code, String sqlType) {
        this.code = code;
        this.sqlType = sqlType;
    }
    public Integer getCode() {
        return code;
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
