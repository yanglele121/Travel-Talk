package com.example.travel.service;

import com.example.travel.bean.UserBean;
import com.example.travel.mapper.FollowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-04-13 14:59
 */
@Service
public class FollowService {
    @Autowired
    FollowMapper followMapper;
    public List<UserBean> AllFollows(Integer id) {
        return followMapper.Allfollows(id);
    }

    public List<UserBean> AllFans(Integer id) {
        return followMapper.AllFans(id);
    }

    public Integer followCounts(Integer id){
        return followMapper.followCounts(id);
    }

    public Integer fanCounts(Integer id){
        return followMapper.fanCounts(id);
    }

    public Boolean isFanOf(Integer userId,Integer fanId){
        return followMapper.isFanOf(userId,fanId) != 0;
    }

    public Integer AddFollow(Integer fanId,Integer userId){
        return followMapper.AddFollow(fanId,userId);
    }

    public Integer DeleteFollow(Integer fanId, Integer followId) {
        return followMapper.DeleteFollow(fanId,followId);
    }
}
