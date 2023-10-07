package com.example.travel.mapper;

/**
 * @author 杨乐乐
 * @time 2023-05-25 13:59
 */
import com.example.travel.bean.ScoreForSiteBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScoreForSiteMapper {

    Integer getUserNum(Integer siteId);
    ScoreForSiteBean getScoreForSite(Integer userId,Integer siteId);
    Integer addScoreForSite(Integer userId,Integer siteId,Float score);
    Integer deleteScoreForSite(Integer userId,Integer siteId);
}