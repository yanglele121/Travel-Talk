package com.example.travel.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class ArticleBean {
    private Integer articleNid;
    private String articleTitle;
    private String articleAbstract;
    private String articleContent;
    private String articleAuthor;
    private String coverUrl;
    private Integer articleRecommend;
    private Integer lookCount;
    private Integer starCount;
    private Integer collectedCount;
    private Integer commentCount;
    private LocalDateTime createDate;
    private Integer articleStatus;
    private Integer siteId;

}
