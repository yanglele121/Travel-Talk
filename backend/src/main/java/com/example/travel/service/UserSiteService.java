
/**
 * @author 杨乐乐
 * @time 2023-05-25 14:03
 */
package com.example.travel.service;

import com.example.travel.bean.UserSiteBean;
import com.example.travel.mapper.UserMapper;
import com.example.travel.mapper.UserSiteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSiteService {

    @Autowired
    UserSiteMapper userSiteMapper;

    public List<UserSiteBean> getUserSiteList(Integer userId) {
        return userSiteMapper.getUserSiteList(userId);
    }
    public boolean getUserSite(Integer userId,Integer siteId){
        UserSiteBean userSiteBean=userSiteMapper.getUserSite(userId,siteId);
        if(userSiteBean==null)
        {
            return false;
        }

        return true;
    }
    public Integer addUserSite(Integer userId,Integer siteId)
    {
        return  userSiteMapper.addUserSite(userId,siteId);
    }
    public Integer deleteUserSite(Integer userId,Integer siteId)
    {
        return  userSiteMapper.deleteUserSite(userId,siteId);
    }

}

