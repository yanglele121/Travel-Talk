package com.example.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 杨乐乐
 * @time 2023-04-17 21:21
 */

// 接收非保密的用户信息
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Integer userNid;
    private String userName;
    private Integer userAge;
    private String userSex;
    private String userAvatarUrl;
    private String userDesc;
    private String userAddr;
}
