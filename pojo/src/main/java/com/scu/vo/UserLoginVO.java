package com.scu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "用户登录返回信息")
public class UserLoginVO {
    private String username;
    private String name;
    private Integer role;
}
