package com.bc208.blog.controller;

import com.bc208.blog.common.dto.Result;
import com.bc208.blog.service.QuartzService;
import com.bc208.blog.utils.CreatTreasureAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/bc208/")
public class QuartzController {

    @Autowired
    private QuartzService quartzService;

    @GetMapping("/addJob")
    public Result quartzJobAdd(){
        Map<String, Object> jobData = new HashMap<>();
        jobData.put("taskName", "testJob");
        jobData.put("beginTime", "new");
        jobData.put("importance", 1);
        jobData.put("taskDesc", "no desc");
        // quartzService.addJob(UserRemindTask.class, "定时任务测试", "user", jobData, "0/10 * * * * ?");
        quartzService.addJob(CreatTreasureAddress.class, "定时任务_宝藏地址", "user", jobData, "0 0 6 * * ?");
        return Result.success();
    }

    @GetMapping("/delJob")
    public Result quartzJobDel(){
        quartzService.deleteJob("定时任务_宝藏地址", "user");
        return Result.success();
    }

    @GetMapping("/runNowJob")
    public Result quartzRunNowJob(){
        quartzService.runAJobNow("定时任务_宝藏地址", "user");
        return Result.success();
    }

}
