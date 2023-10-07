package com.example.travel.service;

import com.example.travel.bean.PicAlbumBean;
import com.example.travel.mapper.PicAlbumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PicAlbumService {
    @Autowired
    PicAlbumMapper picAlbumMapper;
    public List<PicAlbumBean> getPicAlbumList(){
        return  picAlbumMapper.getPicAlbumList();
    };
    public PicAlbumBean getPicAlbumByNid(int picAlbumNid){
        return picAlbumMapper.getPicAlbumByNid(picAlbumNid);
    };
    public List<PicAlbumBean> getPicAlbumListByUserNid(int userNid){
        return picAlbumMapper.getPicAlbumListByUserNid(userNid);
    };

    public int addPicAlbum(PicAlbumBean picAlbumBean){
        return picAlbumMapper.addPicAlbum(picAlbumBean);
    };
    public int updatePicAlbum(PicAlbumBean picAlbumBean){
        return picAlbumMapper.updatePicAlbum(picAlbumBean);
    };
    public int deletePicAlbumByPicAlbumNid(int picAlbumNid){
        return picAlbumMapper.deletePicAlbumByPicAlbumNid(picAlbumNid);
    };

}
