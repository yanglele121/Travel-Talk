package com.example.travel.vo;

import com.example.travel.bean.PicAlbumBean;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//我日你妈妈的picAlbum的返回值
public class PicAlbumVo {
    private int picalbumNid;
    private int userNid;
    private String userName;
    private String userAvatarUrl;
    private String picalbumContent;
    private String imgUrl;
    private int likeCount;

}
