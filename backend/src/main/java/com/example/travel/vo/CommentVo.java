package com.example.travel.vo;

/**
 * @author 杨乐乐
 * @time 2023-05-08 10:14
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private Integer commentNid;
    private String commentContent;
    private Integer userId;
    private Integer articleId;
    private Integer rootCommentId;
    private Integer parentCommentId;
    private Integer starCount;
    private Integer commentCount;
    private LocalDateTime createDate;

    private String userName;
    private String articleTitle;

    private String rootComment;

    private String parentComment;
}
