package cn.edu.bit.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentDto {
    @NotBlank(message = "内容不能为空")
    private String content;
    private Long commentatorId;
    private Long rootId;
}
