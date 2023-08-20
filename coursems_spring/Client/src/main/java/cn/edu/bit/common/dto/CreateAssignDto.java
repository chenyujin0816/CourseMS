package cn.edu.bit.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateAssignDto {

    //发布作业
    @NotBlank(message="标题不能为空")
    private String title;//标题
    @NotBlank(message="描述不能为空")
    private String description;//描述
    private Long courseId;//课程编号
    private String deadline;//截止时间
    private Long week;
}
