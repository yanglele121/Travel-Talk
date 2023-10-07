package com.example.travel.service;

import com.example.travel.bean.AnnouncementBean;
import com.example.travel.mapper.AnnouncementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-05-22 15:07
 */
@Service
public class AnnouncementService {
    @Autowired
    AnnouncementMapper announcementMapper;
    public List<AnnouncementBean> getSysAnnouncement() {
        return announcementMapper.getSysAnnouncement();
    }

    public Integer addAnnouncement(AnnouncementBean announcementBean) {
        return announcementMapper.addAnnouncement(announcementBean);
    }

    public Integer updateAnnouncement(AnnouncementBean announcementBean) {
        return  announcementMapper.updateAnnouncement(announcementBean);
    }

    public List<AnnouncementBean> TopAnnouncement() {
        return announcementMapper.TopAnnouncement();
    }
}
