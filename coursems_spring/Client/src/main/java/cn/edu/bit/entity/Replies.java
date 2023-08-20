package cn.edu.bit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 回复实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Replies implements Serializable {
    private Long id;//ID
    private Long fromId;//from_ID
    private Long toId;//to_ID
    private String content;//内容
    private Long parentId;//从属回帖id
    private Timestamp createTime;//回帖时间
}
