
package com.bc208.blog.service.impl;

import com.bc208.blog.common.dto.Result;
import com.bc208.blog.common.dto.TaskDTO;
import com.bc208.blog.common.vo.TaskVO;
import com.bc208.blog.pojo.UserTask;
import com.bc208.blog.repository.base.mapper.TasksMapper;
import com.bc208.blog.service.QuartzService;
import com.bc208.blog.service.TaskService;
import com.bc208.blog.utils.UserHolder;
import com.bc208.blog.utils.UserRemindTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * @author QingheLi
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TasksMapper tasksMapper;

    private final QuartzService quartzService;

    public TaskServiceImpl(TasksMapper tasksMapper, QuartzService quartzService) {
        this.tasksMapper = tasksMapper;
        this.quartzService = quartzService;
    }

    @Override
    @Transactional
    public Result add(TaskDTO taskDTO) {
        log.warn(String.valueOf(taskDTO));

        UserTask userTask = new UserTask();
        userTask.setTaskName(taskDTO.getTaskName());
        userTask.setUserId(UserHolder.getUser().getUserId());
        userTask.setBeginTime(taskDTO.getBeginTime());
        userTask.setEndTime(taskDTO.getEndTime());
        userTask.setImportance(taskDTO.getImportance());
        userTask.setRemind(taskDTO.getRemind());
        userTask.setRemindTime(taskDTO.getRemindTime());
        userTask.setTaskDesc(taskDTO.getTaskDesc());
        userTask.setTaskDone(0);

        if (taskDTO.getRemind() == 1){
            // 创建一个格式化器，将LocalDateTime转换为Cron表达式需要指定格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy");
            // 将LocalDateTime格式化为Cron表达式字符串
            String cronExpression = taskDTO.getRemindTime().format(formatter);
            HashMap<String, Object> taskData = new HashMap<>(4);
            taskData.put("taskName", taskDTO.getTaskName());
            // 定义日期时间格式
            // 使用格式化器将 LocalDateTime 转为字符串
            taskData.put("beginTime", taskDTO.getBeginTime().toString());
            taskData.put("importance", taskDTO.getImportance());
            taskData.put("taskDesc", taskDTO.getTaskDesc());
            quartzService.addJob(UserRemindTask.class, taskDTO.getTaskName(), UserHolder.getUser().getUserId().toString(), taskData, cronExpression);
        }

        if (tasksMapper.addTask(userTask) != 1){
            return Result.fail("创建任务失败");
        }
        log.info("任务:"+taskDTO.getTaskName()+":创建成功");
        return Result.success();
    }

    @Override
    public Result delete(Long taskId) {
        if(tasksMapper.delete(taskId) != 1){
            return Result.fail("删除任务失败, 请联系管理员.");
        }
        log.info("任务Id:"+taskId+":删除成功");
        return Result.success();
    }

    @Override
    @Transactional
    public Result update(TaskVO taskVO) {
        if (taskVO.getRemind() == 1){
            // 创建一个格式化器，将LocalDateTime转换为Cron表达式需要指定格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy");
            // 将LocalDateTime格式化为Cron表达式字符串
            String cronExpression = taskVO.getRemindTime().format(formatter);
            quartzService.updateJob(taskVO.getTaskName(), UserHolder.getUser().getUserId().toString(), cronExpression);
        }
        if (tasksMapper.update(taskVO) != 1){
            return Result.fail("更新任务错误, 请联系管理员.");
        }
        log.info("任务:"+taskVO.getTaskName()+":更新成功");
        return Result.success();
    }

    @Override
    public Result getTasks(Long userId) {
        List<TaskVO> tasks = tasksMapper.getTasks(userId);
        if (tasks == null){
            return Result.fail("获取任务列表失败, 请联系管理员.");
        }
        return Result.success(tasks);
    }

    @Override
    public Result getTasksDone(Long userId) {
        List<TaskVO> tasks = tasksMapper.getTasksDone(userId);
        if (tasks == null){
            return Result.fail("获取任务列表失败, 请联系管理员.");
        }
        return Result.success(tasks);
    }

    @Override
    public Result taskDoneChange(Long userId) {
        if(tasksMapper.taskDoneChange(userId) == 1){
            return Result.success();
        }else{
            return Result.fail("修改任务错误, 请联系管理员");
        }
    }
}



