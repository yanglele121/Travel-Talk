package com.example.travel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@ToString
public class UserBean {
    private Integer userNid;
    private String userName;
    private String userPassword;
    private Integer userAge;
    private String userSex;
    private String userEmail;
    private String userTel;
    private String userAvatarUrl;
    private String userDesc;
    private LocalDateTime dateJoined;
    private LocalDateTime lastLogin;
    private String userAddr;
    private String userIp;
}
