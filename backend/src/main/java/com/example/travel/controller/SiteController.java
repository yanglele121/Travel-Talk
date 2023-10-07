package com.example.travel.controller;

import com.example.travel.bean.ArticleBean;
import com.example.travel.bean.ScoreForSiteBean;
import com.example.travel.bean.SiteBean;
import com.example.travel.service.ArticleService;
import com.example.travel.service.ScoreForSiteService;
import com.example.travel.service.SiteService;
import com.example.travel.service.UserSiteService;
import com.example.travel.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.lang.Math.min;

@RestController
@RequestMapping("/Site")
public class SiteController {
    @Autowired
    SiteService siteService;

    @Autowired
    ArticleService articleService;

    @Autowired
    UserSiteService userSiteService;

    @Autowired
    ScoreForSiteService scoreForSiteService;

    @GetMapping("/SiteList")
    public Result getSiteList() {

        List<SiteBean> SiteList = siteService.getSiteList();
        if(SiteList.isEmpty()){
            return Result.fail(500,"景点信息获取失败");
        }

        return Result.success(SiteList);
    }
    @PostMapping("/SiteListPart")
    public Result getSiteList(@RequestBody Pageparam pageparam) {
        PageHelper.startPage(pageparam.getPagenum(), pageparam.getPagesize());
        List<SiteBean> SiteList = siteService.getSiteList();
        if (SiteList.isEmpty()) {
            return Result.fail(500, "景点信息获取失败");
        }
        PageInfo<SiteBean> pageInfo = new PageInfo<>(SiteList);
        return Result.success(pageInfo);
    }

    @PostMapping("/getRelatedSiteListByKeyWord")
    public Result getRelatedSiteListByKeyWord(@RequestBody Map<String,Object> params){
        String keyWord = params.get("keyWord").toString();
        int pageNum = Integer.parseInt(params.get("pageNum").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        String selectedPlace = params.get("selectedPlace").toString();
        List<SiteBean> siteList = siteService.getSiteListByTitle(keyWord);
        List<SiteBean> list = new ArrayList<>();
        for (SiteBean siteBean : siteList) {
            if (siteBean.getSiteAddr().contains(selectedPlace) || selectedPlace.isEmpty()) {
                list.add(siteBean);
            }
        }
        List<SiteBean> resList = new ArrayList<>();
        int startIdx = (pageNum-1)*pageSize;
        for(int i=startIdx;i<min(startIdx+pageSize,list.size());i++) {
            resList.add(list.get(i));
        }
        manualPageVo manualPageVo = new manualPageVo(resList,list.size());
        return Result.success(manualPageVo);
    }

    @PostMapping("/getSiteListBySearchWord")
    public Result getSiteListBySearchWord( @RequestBody PageparamAndSearchWord pageparamAndSearchWord) {

        PageHelper.startPage(pageparamAndSearchWord.getPagenum(), pageparamAndSearchWord.getPagesize());

        String searchWord=pageparamAndSearchWord.getSearchword();

        String value=pageparamAndSearchWord.getValue();

        List<SiteBean> resList= new ArrayList<>();

        if(value.equals("选项1"))//按景点名搜索
        {
            resList=siteService.getSiteListByTitle(searchWord);
        }
        else if(value.equals("选项2"))//按地区搜索
        {
            resList=siteService.getSiteListByArea(searchWord);
        }

        if(resList.isEmpty()){
            return Result.fail(505,"景点列表为空");
        }

        PageInfo<SiteBean> pageInfo = new PageInfo<>(resList);

        return Result.success(pageInfo);
    }
    // 获取热门打卡地
    @GetMapping("/HotCity")
    public Result HotCity(){
        List<Map<String,Object>> hotcitys = siteService.getHotCity();
        return Result.success(hotcitys);
    }
    @GetMapping("/HotSite")
    public Result HotSite(){
        List<SiteBean> hotsites = siteService.getHotSite();
        return Result.success(hotsites);
    }
    @GetMapping("/LocalHotSite")
    public Result LocalHotSite(@RequestParam Map<String,Object>params){
        String city = params.get("localCity").toString();
        List<SiteBean> hotsites = siteService.getLocalHotSite(city);
//        if(hotsites.isEmpty()){
//            return Result.fail(577,"你的所在地还没有被推荐的景点哦");
//        }
        return Result.success(hotsites);
    }
//    @GetMapping("/SiteInfo")
//    public Result SiteInfo(@RequestParam Map<String,Object>params){
//        Integer SiteId = Integer.parseInt(params.get("SiteId").toString());
//        Map<String,Object>siteInfo=new HashMap<>();
//        SiteBean siteBean =  siteService.getSiteByNid(SiteId);
//        List<ArticleBean> articleBeanList =  articleService.getArticleBySiteId(SiteId);
//        siteInfo.put("siteInfo",siteBean);
//        siteInfo.put("articles",articleBeanList);
//        return Result.success(siteInfo);
//    }
    @GetMapping("/getSiteByNid/{id}")
    public Result getSiteByNid(@PathVariable("id") Integer id) {
        SiteBean siteBean = siteService.getSiteByNid(id);
        return Result.success(siteBean);
    }

    @PostMapping("/SitesByIds")
    public Result getSitesByIds(@RequestBody Map<String,Object>params){
        List<Integer>idList = (List<Integer>)params.get("idList");
        List<SiteBean> siteBeanList=new ArrayList<>();
        for (Integer id:idList) {
            siteBeanList.add(siteService.getSiteByNid(id));
        }
        return Result.success(siteBeanList);
    }

    @PostMapping(value = "/deleteSiteByNid/{id}")
    public Result deleteSiteByNid(@PathVariable("id") Integer id) {
        SiteBean site = siteService.getSiteByNid(id);
        if (site != null) {
            int status = siteService.deleteSiteByNid(id);

            return Result.success("删除景点成功");
        } else {
            return Result.fail(609, "景点不存在！");
        }

    }

    @PostMapping(value = "/deleteSiteByNids")
    public Result deleteSiteByNids(@RequestBody List<SiteBean> siteBeans) {

        for (int i = 0; i < siteBeans.size(); i++) {

            SiteBean site = siteService.getSiteByNid(siteBeans.get(i).getSiteNid());

            if (site == null) {
                return Result.fail(509, "景点不存在！");
            }

            siteService.deleteSiteByNid(siteBeans.get(i).getSiteNid());


        }
        return Result.success("删除景点成功");

    }

    @RequestMapping(value = "/addSite", method = RequestMethod.POST)
    public Result addSite(@RequestBody SiteBean siteBean) {
        SiteBean site = siteService.getSiteByName(siteBean.getSiteName());

        if (site != null) {

            return Result.fail(606, "景点名称重复！");
        }
        //TODO：默认景点图片->后面需要改成真实上传
        siteBean.setSitePicUrl("https://youimg1.c-ctrip.com/target/10010q000000gkdlr65FB.jpg");
        int status = siteService.addSite(siteBean);

        if (status == 0) {
            return Result.fail(608, "景点添加失败！");
        }
        return Result.success("景点添加成功");

    }

    @RequestMapping(value = "/updateSite", method = RequestMethod.POST)
    public Result updateSite(@RequestBody SiteBean siteBean) {

        SiteBean site = siteService.getSiteByNid(siteBean.getSiteNid());

        if (site != null) {
            site.setSiteName(siteBean.getSiteName());
            site.setSiteAddr(siteBean.getSiteAddr());
            site.setSiteScore(siteBean.getSiteScore());
            site.setSiteDesc(siteBean.getSiteDesc());
            site.setSiteUrl(siteBean.getSiteUrl());

            siteService.updateSite(site);
            return Result.success("景点更新成功");

        }
        return Result.fail(616, "景点不存在！");

    }

    @GetMapping("/getSiteNum")
    public Result getSiteNum(){
        return Result.success(siteService.getSiteNum());
    }

    @RequestMapping(value="/updateSiteUrl")
    public Result updateSiteUrl(@RequestBody Map<String,Object> params) {
        Integer siteId = Integer.parseInt(params.get("siteId").toString());
        String siteUrl = params.get("siteUrl").toString();
        System.out.println(siteId + " " + siteUrl);
        SiteBean siteBean = siteService.getSiteByNid(siteId);
        siteBean.setSitePicUrl(siteUrl);
        siteService.updateSite(siteBean);
        return Result.success(1);
    }

    @PostMapping("/SiteInfo")
    public Result SiteInfo(@RequestBody UserSiteInfoVo userSiteInfoVo){
        Integer SiteId = userSiteInfoVo.getSiteId();
        Map<String,Object>siteInfo=new HashMap<>();
        SiteBean siteBean =  siteService.getSiteByNid(SiteId);
        List<ArticleBean> articleBeanList =  articleService.getArticleBySiteId(SiteId);


        Integer UserId=userSiteInfoVo.getUserId();

        boolean collected=userSiteService.getUserSite(UserId,SiteId);
        boolean scored=scoreForSiteService.getScoreForSite(UserId,SiteId);
        siteInfo.put("collected",collected);
        siteInfo.put("scored",scored);

        siteInfo.put("siteInfo",siteBean);
        siteInfo.put("articles",articleBeanList);

        return Result.success(siteInfo);
    }

    @PostMapping("/AddUserSite")
    public Result AddUserSite(@RequestBody UserSiteInfoVo userSiteInfoVo){
        Integer SiteId = userSiteInfoVo.getSiteId();
        Integer UserId=userSiteInfoVo.getUserId();
        Integer status=userSiteService.addUserSite(UserId,SiteId);
        if(status==0)
        {
            return Result.fail(203,"数据库添加失败！");
        }

        return Result.success("添加成功");
    }
    @PostMapping("/DeleteUserSite")
    public Result DeleteUserSite(@RequestBody UserSiteInfoVo userSiteInfoVo){
        Integer SiteId = userSiteInfoVo.getSiteId();
        Integer UserId=userSiteInfoVo.getUserId();
        Integer status=userSiteService.deleteUserSite(UserId,SiteId);
        if(status==0)
        {
            return Result.fail(204,"数据库删除失败！");
        }

        return Result.success("删除成功");
    }

    @PostMapping("/AddScore")
    public Result AddScore(@RequestBody ScoreForSiteBean scoreForSiteBean){
        Integer SiteId = scoreForSiteBean.getSiteId();
        Integer UserId=scoreForSiteBean.getUserId();
        Float score=scoreForSiteBean.getScore();
        System.out.println(score);
        Integer status= scoreForSiteService.addScoreForSite(UserId,SiteId,score);
        if(status==0)
        {
            return Result.fail(204,"数据库添加失败！");
        }
        SiteBean siteBean=siteService.getSiteByNid(SiteId);
        Float oldSiteScore=siteBean.getSiteScore();
        Integer num=scoreForSiteService.getUserNum(SiteId);
        Float newSiteScore=(oldSiteScore*(num-1)+score)/num;
        siteBean.setSiteScore(newSiteScore);
        siteService.updateSite(siteBean);
        return Result.success("添加成功");
    }

}
