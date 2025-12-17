package com.scu.service.impl;



import cn.hutool.core.util.StrUtil;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.constant.MessageConstant;
import com.scu.constant.PasswordConstant;
import com.scu.constant.SessionKeyConstant;
import com.scu.constant.UserRoleConstant;
import com.scu.dto.UserLoginDTO;
import com.scu.dto.UserRegisterDTO;
import com.scu.entity.User;
import com.scu.exception.*;
import com.scu.mapper.UserMapper;
import com.scu.service.UserService;
import com.scu.vo.UserLoginVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO, HttpSession session) {

        //获取图形验证码
        String checkCode = userLoginDTO.getCheckCode();
        if (checkCode == null||!checkCode.toLowerCase().equals(session.getAttribute(SessionKeyConstant.CHECK_CODE_KEY))){
            throw new CheckCodeErrorException(MessageConstant.CHECK_CODE_ERROR);
        }
        session.removeAttribute(SessionKeyConstant.CHECK_CODE_KEY);
        //获取用户名
        String username = userLoginDTO.getUsername();
        //获取md5加密后的密码
        String password = DigestUtils.md5DigestAsHex(userLoginDTO.getPassword().getBytes());

        //查询用户

        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);

        //用户不存在
        if(user == null) throw new UserNotExistException(MessageConstant.USER_NOT_EXIST);
        //密码错误
        if(!password.equals(user.getPassword())) throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);

        return UserLoginVO.builder().role(user.getRole())
                             .username(user.getUsername())
                             .name(user.getName())
                             .build();

    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        String password= userRegisterDTO.getPassword();
        String passwordConfirm = userRegisterDTO.getPasswordConfirm();
        //用户名密码不能为null
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)||!password.equals(passwordConfirm))
            throw new InvalidRegisterInfoException(MessageConstant.ERROR_REGISTER_INFO);
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class).eq(User::getUsername, username);
        //账号已存在
        if(this.count(wrapper) > 0) throw new AccountExistException(MessageConstant.ACCOUNT_EXIST);
        //保存用户
        User user= User.builder()
                .email(userRegisterDTO.getEmail())
                .username(username)
                .password(DigestUtils.md5DigestAsHex(password.getBytes()))
                .name(userRegisterDTO.getName())
                .role(UserRoleConstant.ROLE_USER)
                .build();
        save(user);
    }

    @Override
    public void resetPwd(String username) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class).eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        if(user == null) throw new UserNotExistException(MessageConstant.USER_NOT_EXIST);
        user.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
    }

    @Override
    public void updatePwd(String username, String password) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate(User.class)
                .eq(User::getUsername, username)
                .set(User::getPassword, DigestUtils.md5DigestAsHex(password.getBytes()));
        update(wrapper);
    }
}
