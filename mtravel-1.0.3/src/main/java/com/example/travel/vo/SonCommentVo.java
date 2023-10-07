package com.example.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SonCommentVo {
    Integer commentId;
    String content;
    String date;
    Integer user_id;
    String username;
    String to_username;
    String userAvatarUrl;
    Integer like_number;
    boolean liked;
}
