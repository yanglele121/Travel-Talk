package com.example.travel.service;

import com.example.travel.bean.SiteBean;
import com.example.travel.mapper.SiteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SiteService {

    @Autowired
    SiteMapper siteMapper;

    public List<SiteBean> getSiteList() {
        return siteMapper.getSiteList();
    }

    public SiteBean getSiteByNid(Integer siteNid) {
        return siteMapper.getSiteByNid(siteNid);
    }

    public int addSite(SiteBean siteBean) {
        return siteMapper.addSite(siteBean);
    }

    public int deleteSiteByNid(int siteNid) { return siteMapper.deleteSiteByNid(siteNid); }

    public int updateSite(SiteBean site) { return siteMapper.updateSite(site); }

    public List<Map<String,Object>> getHotCity() {
        return siteMapper.getHotCity();
    }

    public List<SiteBean> getHotSite() {
        return siteMapper.getHotSite();
    }

    public List<SiteBean> getLocalHotSite(String city) {
        return siteMapper.getLocalHotSite(city);
    }

    public List<SiteBean> getSiteListByTitle(String searchWord) {
        return siteMapper.getSiteListByTitle(searchWord);
    }

    public List<SiteBean> getSiteListByArea(String searchWord) {
        return siteMapper.getSiteListByArea(searchWord);
    }

    public SiteBean getSiteByName(String siteName) {
        return siteMapper.getSiteByName(siteName);
    }

    public Integer getSiteNum() {
        return siteMapper.getSiteNum().get(0);
    }
}
