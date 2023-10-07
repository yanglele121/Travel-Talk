package com.example.travel.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserLikeArticleBean {
    private Integer Nid;
    private Integer userId;
    private Integer articleId;
}
