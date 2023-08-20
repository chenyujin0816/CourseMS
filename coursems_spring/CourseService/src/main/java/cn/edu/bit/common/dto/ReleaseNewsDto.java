package cn.edu.bit.common.dto;

import lombok.Data;

@Data
public class ReleaseNewsDto {

    //发布新闻
    //@NotBlank(message="标题不能为空")
    private String title;//标题
    //@NotBlank(message="内容不能为空")
    private String content;//内容
    private String courseNumber;//课程编号
}
