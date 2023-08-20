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
 * 回帖实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("\"comment_post\"")
public class CommentPost implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value="id",type= IdType.AUTO)
    private Long id;//ID

    @NotBlank(message = "内容不能为空")
    private String content;//内容

    private Long commentatorId;//回帖人id

    private Long rootId;//从属主题帖id

    private Timestamp createTime;//回帖时间
}
