package com.example.travel.mapper;

import com.example.travel.bean.SiteBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SiteMapper {
    List<SiteBean> getSiteList();
    SiteBean getSiteByNid(Integer siteNid);
    int addSite(SiteBean siteBean);
    int deleteSiteByNid(int siteNid);
    int updateSite(SiteBean site);

    List<Map<String,Object>> getHotCity();

    List<SiteBean> getHotSite();

    List<SiteBean> getLocalHotSite(String siteAddr);

    List<SiteBean> getSiteListByTitle(String searchWord);

    List<SiteBean> getSiteListByArea(String searchWord);

    SiteBean getSiteByName(String siteName);

    List<Integer> getSiteNum();
}
