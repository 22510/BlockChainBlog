package com.bc208.blog.controller;

import com.bc208.blog.common.dto.JudgeDto;
import com.bc208.blog.common.dto.LoginDTO;
import com.bc208.blog.common.dto.Result;
import com.bc208.blog.pojo.AdminRegisterDTO;
import com.bc208.blog.service.impl.AdminServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author QingheLi
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@Api(tags = "管理员接口")
public class AdminController {

    @Autowired
    AdminServiceImpl adminService;

    @ApiOperation("管理员注册")
    @PostMapping("/register")
    @ResponseBody
    public Result adminRegister(@RequestBody AdminRegisterDTO adminRegisterDto){
        return adminService.adminRegister(adminRegisterDto);
    }

    @PostMapping("/login")
    @ResponseBody
    public Result adminLogin(@RequestBody LoginDTO admin){
        return adminService.adminLogin(admin);
    }

    @GetMapping("/logout")
    @ResponseBody
    public Result adminLogout(String token) {
        return adminService.adminLogout(token);
    }

    @PostMapping("/judge")
    public Result judgeApplication(@RequestBody JudgeDto judgeDto) {
        return adminService.judgeApplication(judgeDto);
    }

    @GetMapping("/applications")
    public Result getUserApplications(){
        return adminService.getApplication();
    }

    @GetMapping("/members")
    public Result getMembers(){
        return adminService.getMember();
    }

    @GetMapping("/changeAuth")
    public Result changeUserAuth(String userName, Integer userAuth){
        return adminService.changeUserAuth(userName, userAuth);
    }

    @GetMapping("/deleteMember")
    public Result deleteMember(String userName){
        return adminService.deleteMember(userName);
    }

    @GetMapping("/passApplication")
    public Result passApplication(String userName){
        return adminService.passApplication(userName);
    }
}
