/**
 * @author 杨乐乐
 * @time 2023-05-25 13:58
 */
package com.example.travel.service;

import com.example.travel.bean.ScoreForSiteBean;
import com.example.travel.mapper.ScoreForSiteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreForSiteService {
    @Autowired
    ScoreForSiteMapper scoreForSiteMapper;

    public Integer getUserNum(Integer siteId)
    {
        return scoreForSiteMapper.getUserNum(siteId);
    }

    public Boolean getScoreForSite(Integer userId,Integer siteId)
    {
        ScoreForSiteBean scoreForSiteBean=scoreForSiteMapper.getScoreForSite(userId,siteId);
        if(scoreForSiteBean==null)
        {
            return false;
        }
        return true;
    }
    public Integer addScoreForSite(Integer userId,Integer siteId,Float score)
    {
        return scoreForSiteMapper.addScoreForSite(userId,siteId,score);
    }

    public Integer deleteScoreForSite(Integer userId,Integer siteId)
    {
        return scoreForSiteMapper.deleteScoreForSite(userId,siteId);
    }


}

