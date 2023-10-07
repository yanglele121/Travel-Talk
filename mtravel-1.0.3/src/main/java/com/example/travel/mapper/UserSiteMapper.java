package com.example.travel.mapper;

/**
 * @author 杨乐乐
 * @time 2023-05-25 14:04
 */


import com.example.travel.bean.UserSiteBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserSiteMapper {
    List<UserSiteBean> getUserSiteList(Integer userId);

    UserSiteBean getUserSite(Integer userId,Integer siteId);

    Integer addUserSite(Integer userId,Integer siteId);

    Integer deleteUserSite(Integer userId,Integer siteId);
}

