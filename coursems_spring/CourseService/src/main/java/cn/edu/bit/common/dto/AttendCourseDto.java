package cn.edu.bit.common.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AttendCourseDto {
    @NotBlank(message="课程密码不能为空")
    private String coursePwd;//选课密码
}
