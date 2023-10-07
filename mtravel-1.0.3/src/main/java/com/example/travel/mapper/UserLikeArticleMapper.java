package com.example.travel.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLikeArticleMapper {
    Integer isLiked(Integer userId,Integer articleId);
    int addLike(Integer userId,Integer articleId);
    int delLike(Integer userId,Integer articleId);
}
