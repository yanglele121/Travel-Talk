package com.example.travel.bean;

/**
 * @author 杨乐乐
 * @time 2023-05-25 13:56
 */
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ScoreForSiteBean {
    private Integer nid;
    private Integer userId;
    private Integer siteId;
    private Float score;

}
