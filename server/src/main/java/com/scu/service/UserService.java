package com.scu.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.dto.UserLoginDTO;
import com.scu.dto.UserRegisterDTO;
import com.scu.entity.User;
import com.scu.vo.UserLoginVO;
import jakarta.servlet.http.HttpSession;

public interface UserService extends IService<User> {
    UserLoginVO login(UserLoginDTO userLoginDTO, HttpSession session);
    void register(UserRegisterDTO userRegisterDTO);

    void resetPwd(String username);

    void updatePwd(String username, String password);
}
