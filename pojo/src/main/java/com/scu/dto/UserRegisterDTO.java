package com.scu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "用户注册参数")
public class UserRegisterDTO {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private String name;
}
