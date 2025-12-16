package com.scu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "用户")
@TableName("user")
public class User {
    @Schema(description="主键")
    @TableId
    private Long id;
    @Schema(description="账号")
    private String username;
    @Schema(description="密码")
    private String password;
    @Schema(description="邮箱")
    private String email;
    @Schema(description="用户名")
    private String name;
    @Schema(description="角色: 1普通用户 2管理员 3审核")
    private Integer role;
}
