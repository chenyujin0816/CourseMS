package cn.edu.bit.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StudentNumberDto {
    @NotBlank(message="学号不能为空")
    private String userName;//学号
}
