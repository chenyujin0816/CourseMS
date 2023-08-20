package cn.edu.bit.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {

    //登录
    @NotBlank(message="学号不能为空")
    private String username;//手机号
    @NotBlank(message="密码不能为空")
    private String password;//密码
    private char status;//状态
    private Long id;
}
