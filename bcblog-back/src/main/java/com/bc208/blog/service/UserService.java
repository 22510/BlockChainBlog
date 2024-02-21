package com.bc208.blog.service;

import com.bc208.blog.common.dto.LoginDTO;
import com.bc208.blog.common.dto.Result;
import com.bc208.blog.common.dto.UserRegisterDTO;
import com.bc208.blog.common.dto.wxLinkDTO;

/**
 * @author QingheLi
 */
public interface UserService {

    /**
     * 用户登录
     * @param loginDto 登录信息
     * @return 登录结果
     */
    Result userLogin(LoginDTO loginDto);

    /**
     * 用户注册
     * @param userRegisterDto 注册信息
     * @return 返回注册结果
     */
    Result userRegister(UserRegisterDTO userRegisterDto);//注册


    /**
     * 忘记密码
     * @param userEmail 用户邮箱
     * @return 处理结果
     */
    Result userForgotPassword( String userEmail);

    /**
     * 用户退出
     * @param token 前端token
     * @return 处理结果
     */
    Result userLogout(String token);

    /**
     * 发送验证码
     * @param email 用户邮箱
     * @return 返回处理结果
     */
    Result sendCaptcha(String email);

    /**
     * @param wxCode 小程序Code
     * @return 登录结果
     */
    Result userWxLogin(String wxCode);

    /**
     * @param user 网站登录信息
     * @return 登录结果
     */
    Result userWxLink(wxLinkDTO user);

    Result getQRCode();

    Result userSign();

    Result treasureDig(double latitude, double longitude);
}
