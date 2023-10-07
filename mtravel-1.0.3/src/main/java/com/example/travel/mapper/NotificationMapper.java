package com.example.travel.mapper;

import com.example.travel.bean.NotificationBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-05-20 17:12
 */
@Mapper
public interface NotificationMapper {
    List<NotificationBean> getMyNotification(Integer userId);

    Integer checkUserIdNotification(Integer userId);
    Integer insertUserIdNotification(Integer userId,Integer unreadCount);
    Integer updateUnreadCount(Integer userId,Integer flag);

    Integer addNotification(NotificationBean notificationBean);

    Integer getMyUnreadCount(Integer userId);
}

