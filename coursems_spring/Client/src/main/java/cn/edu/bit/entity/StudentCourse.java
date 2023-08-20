package cn.edu.bit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourse {
    private Long id;//ID
    private Long courseId;//课程ID
    private Long studentId;//学生ID
    private int courseWeek;//周数
    private String url;//文件
    private int score; //评分
}
