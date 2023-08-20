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
 * 讨论帖实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("\"forum_post\"")
public class ForumPost implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value="id",type= IdType.AUTO)
    private Long id;//ID

    @NotBlank(message = "标题不能为空")
    private String title;//标题

    private String description;//描述

    private Long creatorId;//发帖人id
    private char creatorType;//发帖人类别
    private char visibility;//可见性

    private Timestamp createTime;//发布时间
    private Timestamp updateTime;//更新时间


}
