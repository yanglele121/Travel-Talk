package com.example.travel.controller;

import com.example.travel.bean.NotificationBean;
import com.example.travel.service.NotificationService;
import com.example.travel.vo.Result;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-05-20 17:06
 */
@RestController
@RequestMapping("/Notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @GetMapping("/MyNotification/{id}")
    public Result getMyNotification(@PathVariable("id") Integer id){
        List<NotificationBean> resList = notificationService.getMyNotification(id);
        return Result.success(resList);
    }

    /**
     * 向用户发送信息
     * @param notificationBean
     * @return Result
     * 当向用户发送消息时，首先向userNotification表中未读消息+1
     * 将通知写入通知表中
     */
    @PostMapping("/AuditOpinion")
    public Result sendAuditOpinion(@RequestBody NotificationBean notificationBean){
        notificationBean.setCreateDate(LocalDateTime.now());
        notificationService.updateUnreadCount(notificationBean.getUserId(), 1);
        Integer res;
        res = notificationService.addNotification(notificationBean);
        if(res!=0){
            return Result.success(1);
        }
        return  Result.success(0);
    }

    @GetMapping("/MyUnreadCount/{id}")
    public Result getMyUnreadCount(@PathVariable("id") Integer id){
        Integer res = notificationService.getMyUnreadCount(id);
        return Result.success(res);
    }

    @PostMapping("/AllRead/{id}")
    public Result AllRead(@PathVariable("id") Integer id){
        Integer unReadCount = notificationService.getMyUnreadCount(id);
        if(unReadCount==null){
            unReadCount=0;
        }
        notificationService.updateUnreadCount(id,-unReadCount);
        return  Result.success(null);
    }
}
