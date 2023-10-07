package com.example.travel.service;

/**
 * @author 杨乐乐
 * @time 2023-05-24 19:15
 */

import com.example.travel.mapper.UserLikePicAlbumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLikePicAlbumService {
    @Autowired
    UserLikePicAlbumMapper userLikePicAlbumMapper;
    public Boolean isLiked(Integer userId,Integer picAlbumId){
        return userLikePicAlbumMapper.isLiked(userId,picAlbumId) != 0;
    }

    public int addLike(Integer userId,Integer picAlbumId){
        return userLikePicAlbumMapper.addLike(userId,picAlbumId);
    }

    public int delLike(Integer userId,Integer picAlbumId){
        return userLikePicAlbumMapper.delLike(userId,picAlbumId);
    }
}

