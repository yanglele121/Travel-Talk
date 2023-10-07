package com.example.travel.controller;

import com.example.travel.bean.AnnouncementBean;
import com.example.travel.service.AnnouncementService;
import com.example.travel.vo.Pageparam;
import com.example.travel.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-05-22 15:06
 */
@RestController
@RequestMapping("/Announcement")
public class AnnouncementController {
    @Autowired
    AnnouncementService announcementService;

    @PostMapping("/SysAnnouncement")
    public Result getSysAnnouncement(@RequestBody Pageparam pageparam){
        PageHelper.startPage(pageparam.getPagenum(), pageparam.getPagesize());
        List<AnnouncementBean> resList=announcementService.getSysAnnouncement();
        PageInfo<AnnouncementBean>pageInfo = new PageInfo<>(resList);
        return Result.success(pageInfo);
    }
    @PostMapping("/NewAnnouncement")
    public Result addSysAnnouncement(@RequestBody AnnouncementBean announcementBean){
        announcementBean.setCreateDate(LocalDateTime.now());
        Integer status =  announcementService.addAnnouncement(announcementBean);
        if(status!=0){
            return  Result.success("添加成功");
        }
        else{
            return Result.fail(9898,"添加失败");
        }
    }
    @PostMapping("/UpdateNewAnnouncement")
    public Result updateAnnouncement(@RequestBody AnnouncementBean announcementBean){
        announcementBean.setCreateDate(LocalDateTime.now());
        Integer status = announcementService.updateAnnouncement(announcementBean);
        if(status!=0){
            return  Result.success("更新成功");
        }
        else{
            return Result.fail(9898,"更新失败");
        }
    }
    @GetMapping("/TopAnnouncement")
    public Result TopAnnouncement(){
        List<AnnouncementBean>resList = announcementService.TopAnnouncement();
        return  Result.success(resList);
    }
}
