package com.example.travel.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserLikeCommentBean {
    private Integer Nid;
    private Integer userId;
    private Integer commentId;
}
