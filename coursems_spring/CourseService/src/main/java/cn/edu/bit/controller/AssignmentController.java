package cn.edu.bit.controller;

import cn.edu.bit.common.dto.CreateAssignDto;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.entity.Assignment;
import cn.edu.bit.feign.FeignService;
import cn.edu.bit.service.AssignmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/assignment")
public class AssignmentController {
    @Autowired
    AssignmentService assignmentService;
    @Autowired
    FeignService feignService;

    //查看作业详情
    @GetMapping("/assign")
    public Result assignDetail(@RequestParam Long courseId,@RequestParam Long week){
        Assignment assignment = assignmentService.getOne(new QueryWrapper<Assignment>().eq("course_id",courseId).eq("week",week));
        if(assignment==null)
            return Result.fail("该作业已被删除");
        return Result.succ(assignment);
    }

    //发布作业
    @PostMapping("/createAssignment")
    public Result createAssignment(@Validated @RequestBody CreateAssignDto createAssignDto){
        int curUserId = this.getCurUserId();
        if(this.getCurUserType()=='1')
            return Result.fail("没有权限");
        Assignment temp=assignmentService.getOne(new QueryWrapper<Assignment>().eq("course_id",createAssignDto.getCourseId())
                .eq("week",createAssignDto.getWeek()));
        if(temp!=null)
        {
            temp.setTeacherId((long) curUserId);
            temp.setTitle(createAssignDto.getTitle());
            temp.setDeadline(Timestamp.valueOf(createAssignDto.getDeadline()));
            temp.setDescription(createAssignDto.getDescription());
            temp.setCreateTime(new Timestamp(System.currentTimeMillis()));
            temp.setCourseId(createAssignDto.getCourseId());
            temp.setWeek(createAssignDto.getWeek());
            assignmentService.update(temp, new QueryWrapper<Assignment>().eq("course_id",createAssignDto.getCourseId())
                    .eq("week",createAssignDto.getWeek()));

            return Result.succ("更新了发布作业的内容");
        }
        Assignment assignment=new Assignment();
        assignment.setTeacherId((long) curUserId);
        assignment.setTitle(createAssignDto.getTitle());
        assignment.setDeadline(Timestamp.valueOf(createAssignDto.getDeadline()));
        assignment.setDescription(createAssignDto.getDescription());
        assignment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        assignment.setCourseId(createAssignDto.getCourseId());
        assignment.setWeek(createAssignDto.getWeek());

        assignmentService.save(assignment);

        return Result.succ("作业发布成功");
    }

    public int getCurUserId(){
        return feignService.getCurUserId();
    }

    public char getCurUserType(){
        return feignService.getCurUserType();
    }

}
