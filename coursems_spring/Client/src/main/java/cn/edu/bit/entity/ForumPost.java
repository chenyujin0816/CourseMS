package cn.edu.bit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 讨论帖实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumPost implements Serializable {
    private Long id;//ID
    private String title;//标题
    private String description;//描述
    private Long creatorId;//发帖人id
    private char creatorType;//发帖人类别
    private char visibility;//可见性
    private Timestamp createTime;//发布时间
    private Timestamp updateTime;//更新时间
}
