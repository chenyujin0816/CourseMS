package cn.edu.bit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 作业实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignment implements Serializable {
    private Long id;//ID

    private String title;//作业标题

    private String description;//作业描述

    private Long teacherId;//教师id

    private Timestamp deadline;//截止时间

    private Timestamp createTime;//布置时间

    private Long courseId;

    private Long week;
}
