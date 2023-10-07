package com.example.travel.mapper;

/**
 * @author 杨乐乐
 * @time 2023-05-24 19:14
 */

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLikePicAlbumMapper {
    Integer isLiked(Integer userId,Integer picAlbumId);
    int addLike(Integer userId,Integer picAlbumId);
    int delLike(Integer userId,Integer picAlbumId);
    int delPicAlbum(Integer picAlbumId);
}
