package cn.edu.bit.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginStatusDto implements Serializable {

    private Long id;
    private char loginStatus;

}
