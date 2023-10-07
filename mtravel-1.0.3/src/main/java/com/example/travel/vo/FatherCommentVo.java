package com.example.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FatherCommentVo {
    Integer commentId;
    String content;
    String date;
    Integer comment_num;
    Integer like_number;
    boolean liked;
    Integer user_id;
    String username;
    String userAvatarUrl;
    boolean fallbacked;
    List<SonCommentVo>sonCommentList;
}
