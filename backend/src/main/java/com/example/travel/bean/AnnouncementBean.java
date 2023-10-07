package com.example.travel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 杨乐乐
 * @time 2023-05-22 14:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementBean {
    private Integer announcementNid;
    private String announcementTitle;
    private String announcementContent;
    private LocalDateTime createDate;
    private String coverUrl;
}
