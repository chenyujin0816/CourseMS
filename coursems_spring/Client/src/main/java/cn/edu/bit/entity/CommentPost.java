package cn.edu.bit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 回帖实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPost implements Serializable {
    private Long id;//ID

    private String content;//内容

    private Long commentatorId;//回帖人id

    private Long rootId;//从属主题帖id

    private Timestamp createTime;//回帖时间
}
