package com.scu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.scu.common.CheckCode;
import com.scu.common.Result;

import com.scu.dto.DeleteBatchDTO;
import com.scu.dto.UserLoginDTO;
import com.scu.dto.UserRegisterDTO;
import com.scu.entity.User;
import com.scu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "获取验证码")
    @GetMapping("/checkCode")
    public void checkCode(HttpServletResponse  response, HttpSession  session) throws IOException {
        CheckCode checkCode = new CheckCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = checkCode.getCode();
        session.setAttribute("check_code_key", code);
        checkCode.write(response.getOutputStream());
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO){
        userService.register(userRegisterDTO);
        return Result.success();
    }


    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO, HttpSession session){
        return Result.success(userService.login(userLoginDTO,session));
    }

    /**
     * 查询用户
     */
    @Operation(summary = "查询用户")
    @GetMapping("/info/{username}")
    public Result getUserInfo(@PathVariable("username") String username){
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class).eq(User::getUsername, username);
        User user = userService.getOne(wrapper);
        return Result.success(user);
    }

    /**
     * 重置密码 默认为123456
     */
    @Operation(summary = "重置密码")
    @GetMapping("/resetPwd")
    public Result resetPwd(@RequestParam String username) {
        userService.resetPwd(username);
        return Result.success();
    }

    /**
     * 修改密码
     *
     */
    @Operation(summary = "修改密码")
    @GetMapping("/updatePwd")
    public Result updatePwd(@RequestParam String username, @RequestParam String password) {
        userService.updatePwd(username, password);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/delete")
    public Result delete(@RequestBody DeleteBatchDTO deleteBatchDTO){
        userService.removeByIds(Arrays.asList(deleteBatchDTO.getIds()));
        return Result.success();
    }


    //TODO 获取所有用户
//    @Operation(summary = "获取所有用户")
//    @PostMapping ("/list")
//    public Result list(@RequestBody PageInfoDto pageInfoDto){
//        IPage<User> page = userService.page(new Page<>(pageInfoDto.getPageNum(), pageInfoDto.getPageSize()));
//        return Result.success(page);
//    }


}
