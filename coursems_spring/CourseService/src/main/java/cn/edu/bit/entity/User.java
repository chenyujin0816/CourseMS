package cn.edu.bit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("\"user\"")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type= IdType.AUTO)
    private Long id;//ID

    @NotBlank(message = "用户名不能为空")
    private String username;//用户名（学工号）

    @NotBlank(message = "密码不能为空")
    private String password;//密码

    private char status;//用户状态

    private char type;//用户类型

    private String name;//用户姓名

    private String classNumber;//班级编号

    private String time;//最后一次登录时间

    private Timestamp createTime;//创建时间

    private char loginStatus;//登录状态

}
