package cn.edu.bit.common.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddCourseDto {
    @NotBlank(message="课程编号不能为空")
    private String courseNumber;//课程编号
    @NotBlank(message="课程名称不能为空")
    private String courseName;//选课名称
    @NotBlank(message="课程描述不能为空")
    private String courseInfo;//选课描述
    @NotBlank(message="课程学分不能为空")
    private String courseScore;//课程学分

    private String courseStart;//开课时间
    private Integer courseWeek;//课程周数
    @NotBlank(message="课程密码不能为空")
    private String coursePwd;//选课密码
}
