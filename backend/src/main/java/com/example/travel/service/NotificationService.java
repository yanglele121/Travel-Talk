package com.example.travel.service;

import com.example.travel.bean.NotificationBean;
import com.example.travel.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-05-20 17:07
 */
@Service
public class NotificationService {
    @Autowired
    NotificationMapper notificationMapper;
    public List<NotificationBean> getMyNotification(Integer id) {
        return notificationMapper.getMyNotification(id);
    }
    public Integer updateUnreadCount(Integer id,Integer flag){
        Integer isExist = notificationMapper.checkUserIdNotification(id);
        Integer res;
        if(isExist!=0){
            res=notificationMapper.updateUnreadCount(id,flag);
        }
        else{
            res= notificationMapper.insertUserIdNotification(id,1);
        }
        return res;
    }

    public Integer addNotification(NotificationBean notificationBean) {
        return notificationMapper.addNotification(notificationBean);
    }

    public Integer getMyUnreadCount(Integer id) {
        return notificationMapper.getMyUnreadCount(id);
    }
}
