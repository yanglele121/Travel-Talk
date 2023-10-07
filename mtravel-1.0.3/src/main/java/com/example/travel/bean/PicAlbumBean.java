package com.example.travel.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PicAlbumBean {
    private int picalbumNid;
    private int userId;
    private String picalbumContent;
    private String imgUrl;
    private int likeCount;
}
