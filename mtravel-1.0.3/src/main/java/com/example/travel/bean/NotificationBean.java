package com.example.travel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * @author 杨乐乐
 * @time 2023-05-20 17:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationBean {
    private Integer id;
    private Integer userId;
    private String title;
    private String relatedArticle;
    private LocalDateTime createDate;
    private String content;
}
