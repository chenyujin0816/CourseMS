package cn.edu.bit.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {
    private int id;//用户id
    private String username;//学工号
    private char type;//用户类型
    private String name;//姓名
    private String classNumber;//班级编号
}
