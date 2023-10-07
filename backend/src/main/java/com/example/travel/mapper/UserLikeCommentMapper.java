package com.example.travel.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLikeCommentMapper {
    Integer isLiked(Integer userId,Integer commentId);
    int addLike(Integer userId,Integer commentId);
    int delLike(Integer userId,Integer commentId);
    int delComment(Integer commentId);
}
