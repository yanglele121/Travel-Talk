package com.example.travel.controller;

import com.example.travel.bean.ArticleBean;
import com.example.travel.bean.PicAlbumBean;
import com.example.travel.bean.UserBean;
import com.example.travel.service.PicAlbumService;
import com.example.travel.service.UserService;
import com.example.travel.vo.Pageparam;
import com.example.travel.vo.PicAlbumVo;
import com.example.travel.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/picAlbum")
public class PicAlbumController {
    @Autowired
    PicAlbumService picAlbumService;
    @Autowired
    UserService userService;
    private PicAlbumBean coverAllAttribute(Map<String, Object> params, PicAlbumBean picAlbumBean, boolean isCreate){
        if(params.get("userId") != null){
            picAlbumBean.setUserId(Integer.parseInt(params.get("userId").toString()));
        }
        if(params.get("picAlbumContent") != null){
            picAlbumBean.setPicalbumContent(params.get("picAlbumContent").toString());
        }
        if(params.get("imgUrl") != null){
            picAlbumBean.setImgUrl(params.get("imgUrl").toString());
        }
        return picAlbumBean;
    }

    @PostMapping("/picAlbumList")
    public Result getPicAlbumList(@RequestBody Pageparam pageparam){
        PageHelper.startPage(pageparam.getPagenum(), pageparam.getPagesize());
        List<PicAlbumBean> picAlbumList = picAlbumService.getPicAlbumList();
        if (picAlbumList.isEmpty()) {
            return Result.fail(537, "相片集获取失败");
        }
        List<PicAlbumVo>picAlbumVoList = new ArrayList<>();
        for(int i=0;i<picAlbumList.size();i++){
            Integer userNid = picAlbumList.get(i).getUserId();
            UserBean user = userService.getUserByNid(userNid);
            PicAlbumVo picAlbumVo = new PicAlbumVo(picAlbumList.get(i).getPicalbumNid(),user.getUserNid(),user.getUserName(),
                    user.getUserAvatarUrl(),picAlbumList.get(i).getPicalbumContent(),picAlbumList.get(i).getImgUrl(),picAlbumList.get(i).getLikeCount());
            picAlbumVoList.add(picAlbumVo);
        }
        PageInfo<PicAlbumVo> pageInfo = new PageInfo<>(picAlbumVoList);
        return Result.success(pageInfo);
    }
    @RequestMapping(value = "/addPicAlbum", method= RequestMethod.POST)
    public Result addPicAlbum(@RequestBody PicAlbumBean picAlbumBean){


        Integer status = picAlbumService.addPicAlbum(picAlbumBean);
        if(status == 0){
            return Result.fail(678,"添加失败");
        }
        return Result.success("添加成功");
    }

    @RequestMapping(value = "/getPicAlbumListByUserId/{id}", method= RequestMethod.POST)
    public Result getPicAlbumListByUserNid(@PathVariable("id") Integer userId, @RequestBody Pageparam pageparam){
        PageHelper.startPage(pageparam.getPagenum(), pageparam.getPagesize());
        List<PicAlbumBean> picAlbumList = picAlbumService.getPicAlbumListByUserNid(userId);
        if (picAlbumList.isEmpty()) {
            return Result.success(null);
        }
        PageInfo<PicAlbumBean> pageInfo = new PageInfo<>(picAlbumList);
        return Result.success(pageInfo);
    }
}
