package com.bc208.blog.controller;

import com.bc208.blog.common.dto.LoginDTO;
import com.bc208.blog.common.dto.Result;
import com.bc208.blog.common.dto.UserRegisterDTO;
import com.bc208.blog.common.dto.wxLinkDTO;
import com.bc208.blog.service.impl.UsersServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author QingheLi
 */
@Slf4j
@RestController
@RequestMapping("/bc208/")
public class UserController {

    private final UsersServiceImpl usersServiceImpl;

    public UserController(UsersServiceImpl usersServiceImpl) {
        this.usersServiceImpl = usersServiceImpl;
    }

    @PostMapping("/login")
    @ResponseBody
    public Result userLogin(@RequestBody LoginDTO user){
        return usersServiceImpl.userLogin(user);
    }

    @PostMapping("/register")
    @ResponseBody
    public Result userRegister(@RequestBody UserRegisterDTO userRegisterDto){
        return usersServiceImpl.userRegister(userRegisterDto);
    }

    @GetMapping("/captcha")
    public Result sendCaptcha(String email){
        return usersServiceImpl.sendCaptcha(email);
    }

    @GetMapping("/ForgotPassword")
    @ResponseBody
    public Result forgotPassword(String email){
        return usersServiceImpl.userForgotPassword(email);
    }

    @GetMapping("/logout")
    @ResponseBody
    public Result userLogout(String token) {
        return usersServiceImpl.userLogout(token);
    }

    @GetMapping("/wxLogin")
    @ResponseBody
    public Result wxLogin(@RequestParam("code") String wxCode) {
        return usersServiceImpl.userWxLogin(wxCode);
    }

    @PostMapping("/wxLink")
    @ResponseBody
    public Result wxLink(@RequestBody wxLinkDTO user){
        return usersServiceImpl.userWxLink(user);
    }

    @GetMapping("wxQRCode")
    public Result getWxQRCode(){
        return usersServiceImpl.getQRCode();
    }

    @GetMapping("/hello")
    @ResponseBody
    public Result hello(){
        System.out.println("test");
        return Result.success("hello bc208");
    }

    @GetMapping("/sign")
    public Result userSign(){
        return usersServiceImpl.userSign();
    }

    @GetMapping("/treasure")
    public Result userTreasureDig(@RequestParam("lat") double latitude, @RequestParam("lon") double longitude){
        return usersServiceImpl.treasureDig(latitude, longitude);
    }

}
