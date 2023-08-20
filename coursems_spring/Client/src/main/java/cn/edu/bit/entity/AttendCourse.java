package cn.edu.bit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 选课记录实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendCourse implements Serializable {
    private Long id;//ID
    private Long courseId;//课程ID
    private Long studentId;//学生ID
    private Long studyTime;//活跃次数
    private int grade;//成绩
    private Timestamp createTime;//选课时间

}
