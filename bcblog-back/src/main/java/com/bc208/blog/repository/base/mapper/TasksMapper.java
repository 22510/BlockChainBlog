package com.bc208.blog.repository.base.mapper;

import com.bc208.blog.common.vo.TaskVO;
import com.bc208.blog.pojo.UserTask;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TasksMapper {
    // List<UserTask> queryUserTodolistById(@Param("task_userid") Integer task_userid ,
    //                                      @Param("task_type") Integer task_type );
    //
    // int updateTaskType(@Param("task_userid")Integer task_userid,
    //                    @Param("taskId") Integer taskId,
    //                    @Param("task_type") Integer task_type,
    //                    @Param("finishTime") LocalDateTime finishTime);
    //
    // UserTask queryTask(@Param("task_userid") Integer task_userid ,
    //                    @Param("taskId") Integer taskId);
    //
    // int insertTask(@Param("task_userid")Integer task_userid,
    //                @Param("task_content") String task_content);
    //
    // int updateTask(@Param("task_userid")Integer task_userid,
    //                @Param("taskId") Integer taskId,
    //                @Param("task_content") String task_content);
    //
    // int deleteTask(@Param("task_userid")Integer task_userid,
    //                @Param("taskId") Integer taskId);

    int addTask(UserTask taskDTO);

    int delete(Long taskId);

    int update(TaskVO taskVO);

    List<TaskVO> getTasks(Long userId);

    List<TaskVO> getTasksDone(Long userId);

    int taskDoneChange(Long userId);
}
