package cn.edu.bit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 新闻实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class News implements Serializable {
    private Long id;//ID

    private String title;//标题

    private String content;//内容

    private Long creatorId;//发布人ID

    private Long CourseId;

    private Timestamp createTime;//发布时间

}