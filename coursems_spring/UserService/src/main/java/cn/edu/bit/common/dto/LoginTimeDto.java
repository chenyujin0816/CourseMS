package cn.edu.bit.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginTimeDto implements Serializable {

    private Long id;
    private String time;

}
