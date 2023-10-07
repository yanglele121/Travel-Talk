package com.example.travel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentBean {
    private int commentNid;
    private String commentContent;
    private int userId;
    private int articleId;
    private int rootCommentId;
    private int parentCommentId;
    private int starCount;
    private int commentCount;
    private LocalDateTime createDate;
}
