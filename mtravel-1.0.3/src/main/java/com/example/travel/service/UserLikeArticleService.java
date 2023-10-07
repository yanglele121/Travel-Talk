package com.example.travel.service;

import com.example.travel.mapper.FollowMapper;
import com.example.travel.mapper.UserLikeArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLikeArticleService {
    @Autowired
    UserLikeArticleMapper userLikeArticleMapper;
    public Boolean isLiked(Integer userId,Integer articleId){
        return userLikeArticleMapper.isLiked(userId,articleId) != 0;
    }

    public int addLike(Integer userId,Integer articleId){
        return userLikeArticleMapper.addLike(userId,articleId);
    }

    public int delLike(Integer userId,Integer articleId){
        return userLikeArticleMapper.delLike(userId,articleId);
    }
}
