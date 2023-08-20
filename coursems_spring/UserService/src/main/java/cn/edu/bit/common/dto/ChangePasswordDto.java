package cn.edu.bit.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class ChangePasswordDto implements Serializable {

    private Long id;
    @NotBlank(message="密码不能为空")
    private String password;//密码
}
