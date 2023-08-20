package cn.edu.bit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 课程实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("\"course\"")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value="id",type= IdType.AUTO)
    private Long id;//ID

    private String courseNumber;//课程编号
    private String courseName;//课程名称
    private String courseInfo;//课程描述
    private String courseScore;//课程学分
    private String coursePwd;//选课密码
    private char courseStatus;//课程状态
    private Long teacherId;//教师Id
    private Timestamp courseStart; //开始时间
    private int courseWeek; //周数
    private Timestamp createTime; //创建时间
}
