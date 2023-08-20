package cn.edu.bit.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RepliesDto {

    //回复
    private Long toId;

    @NotBlank(message = "内容不能为空")
    private String content;//内容

    private Long parentId;//从属回帖id
}
