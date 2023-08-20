package cn.edu.bit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private Long id;//ID
    private String username;//用户名（学工号）
    private String password;//密码
    private char status;//用户状态
    private char type;//用户类型
    private String name;//用户姓名
    private String classNumber;//班级编号
    private String time;//最后一次登录时间
    private Timestamp createTime;//创建时间
    private char loginStatus;//登录状态

}
