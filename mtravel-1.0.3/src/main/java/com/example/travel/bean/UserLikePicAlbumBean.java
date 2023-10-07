package com.example.travel.bean;

/**
 * @author 杨乐乐
 * @time 2023-05-24 19:08
 */


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserLikePicAlbumBean {
    private Integer Nid;
    private Integer userId;
    private Integer picAlbumId;
}

