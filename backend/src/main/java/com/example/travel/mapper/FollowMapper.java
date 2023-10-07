package com.example.travel.mapper;

import com.example.travel.bean.UserBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-04-13 17:01
 */
@Mapper
public interface FollowMapper {
    List<UserBean> Allfollows(Integer fanId);

    List<UserBean> AllFans(Integer userId);
    Integer followCounts(Integer fanId);

    Integer fanCounts(Integer userId);
    Integer isFanOf(Integer userId,Integer fanId);
    Integer AddFollow(Integer fanId,Integer userId);

    Integer DeleteFollow(Integer fanId, Integer userId);
}
