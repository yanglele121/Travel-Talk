package com.example.travel.mapper;

import com.example.travel.bean.AnnouncementBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-05-22 15:07
 */
@Mapper
public interface AnnouncementMapper {
    List<AnnouncementBean> getSysAnnouncement();

    Integer addAnnouncement(AnnouncementBean announcementBean);

    Integer updateAnnouncement(AnnouncementBean announcementBean);

    List<AnnouncementBean> TopAnnouncement();
}
