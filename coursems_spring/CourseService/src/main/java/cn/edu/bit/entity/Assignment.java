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
 * 作业实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("\"assignment\"")
public class Assignment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value="id",type= IdType.AUTO)
    private Long id;//ID

    @NotBlank(message = "作业标题不能为空")
    private String title;//作业标题

    private String description;//作业描述

    private Long teacherId;//教师id

    private Timestamp deadline;//截止时间

    private Timestamp createTime;//布置时间

    private Long courseId;

    private Long week;
}
