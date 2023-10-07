package com.example.travel.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteBean {
    private int siteNid;
    private String siteName;
    private String siteAddr;
    private Float siteScore;
    private String siteDesc;
    private String sitePicUrl;
    private String siteUrl;
}
