package cn.edu.bit.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class RegisterDto implements Serializable {

    //注册
    @NotBlank(message="用户名不能为空")
    private String username;//学工号
    @NotBlank(message="密码不能为空")
    private String password;//密码
    @NotBlank(message = "确认密码不能为空")
    private String confirmPwd;//确认密码
    private char type;//用户类型
    @NotBlank(message = "姓名不能为空")
    private String name;
    private String classNumber;//班级编号
}
