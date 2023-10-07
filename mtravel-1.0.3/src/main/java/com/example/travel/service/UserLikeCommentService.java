package com.example.travel.service;

import com.example.travel.mapper.FollowMapper;
import com.example.travel.mapper.UserLikeArticleMapper;
import com.example.travel.mapper.UserLikeCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLikeCommentService {
    @Autowired
    UserLikeCommentMapper userLikeCommentMapper;
    public Boolean isLiked(Integer userId,Integer commentId){
        return userLikeCommentMapper.isLiked(userId,commentId) != 0;
    }

    public int addLike(Integer userId,Integer commentId){
        return userLikeCommentMapper.addLike(userId,commentId);
    }

    public int delLike(Integer userId,Integer commentId){
        return userLikeCommentMapper.delLike(userId,commentId);
    }
}
