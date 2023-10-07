package com.example.travel.service;

import com.example.travel.bean.ArticleBean;
import com.example.travel.bean.CollectBean;
import com.example.travel.mapper.CollectMapper;
import com.example.travel.mapper.FollowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectService {
    @Autowired
    CollectMapper collectMapper;

    public Boolean isCollected(Integer userId,Integer articleId){
        return collectMapper.isCollected(userId,articleId) != 0;
    }

    public int addCollect(Integer userId,Integer articleId){
        return collectMapper.addCollect(userId,articleId);
    }

    public int delCollect(Integer userId,Integer articleId){
        return collectMapper.delCollect(userId,articleId);
    }

    public List<CollectBean> getCollectArticleListByUserId(Integer userId){
        return collectMapper.getCollectArticleListByUserId(userId);
    }
    public Boolean isCollectedSite(Integer userId,Integer siteId){
        return collectMapper.isCollectedSite(userId,siteId) != 0;
    }

    public int addCollectSite(Integer userId,Integer articleId){
        return collectMapper.addCollectSite(userId,articleId);
    }

    public int delCollectSite(Integer userId,Integer articleId){
        return collectMapper.delCollectSite(userId,articleId);
    }

    public List<CollectBean> getCollectSiteListByUserId(Integer userId){
        return collectMapper.getCollectSiteListByUserId(userId);
    }
}
