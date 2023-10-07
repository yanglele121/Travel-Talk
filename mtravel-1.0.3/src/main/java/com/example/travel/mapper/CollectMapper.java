package com.example.travel.mapper;

import com.example.travel.bean.CollectBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectMapper {
    Integer isCollected(Integer userId,Integer articleId);
    int addCollect(Integer userId,Integer articleId);
    int delCollect(Integer userId,Integer articleId);
    List<CollectBean> getCollectArticleListByUserId(Integer userId);
    Integer isCollectedSite(Integer userId,Integer siteId);
    int addCollectSite(Integer userId,Integer siteId);
    int delCollectSite(Integer userId,Integer siteId);
    List<CollectBean> getCollectSiteListByUserId(Integer userId);
}
