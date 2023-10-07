package com.example.travel.mapper;

import com.example.travel.bean.PicAlbumBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PicAlbumMapper {
    List<PicAlbumBean> getPicAlbumList();
    PicAlbumBean getPicAlbumByNid(int picAlbumNid);
    List<PicAlbumBean> getPicAlbumListByUserNid(int userNid);
    int addPicAlbum(PicAlbumBean picAlbumBean);
    int updatePicAlbum(PicAlbumBean picAlbumBean);
    int deletePicAlbumByPicAlbumNid(int picAlbumNid);
}
