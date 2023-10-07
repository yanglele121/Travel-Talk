package com.example.travel.controller;

import com.example.travel.bean.UserBean;
import com.example.travel.service.FollowService;
import com.example.travel.service.LoginService;
import com.example.travel.vo.ErrorCode;
import com.example.travel.vo.Result;
import com.example.travel.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.travel.controller.UserController.AssertUserVo;

/**
 * @author 杨乐乐
 * @time 2023-04-13 14:58
 */
@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    FollowService followService;
    @Autowired
    LoginService loginService;

    /*TODO:获取关注列表   checked
           获取粉丝列表   checked
           关注某人
           取关某人
    */
    //根据用户id找到其关注列表 find follow_list by user id
    @GetMapping("/myfollows/{id}")
    public Result AllFollows(@PathVariable("id") Integer id){
        List<UserVo> follows = new ArrayList<>();
        List<UserBean> allfollows = followService.AllFollows(id);
        for (UserBean follow : allfollows) {
            follows.add(AssertUserVo(follow));
        }
        return Result.success(follows);
    }

    @GetMapping("/myfans/{id}")
    public Result AllFans(@PathVariable("id") Integer id){
        List<UserVo> fans = new ArrayList<>();
        List<UserBean> allfans = followService.AllFans(id);
        for (UserBean fan : allfans) {
            fans.add(AssertUserVo(fan));
        }
        return Result.success(fans);
    }

    @GetMapping("/count/{id}")
    public Result FollowAndFanCounts(@PathVariable("id") Integer id){
        Map<String,Object>resultMap=new HashMap<>();
        Integer followCounts = followService.followCounts(id);
        Integer fanCounts = followService.fanCounts(id);
        resultMap.put("followCounts",followCounts);
        resultMap.put("fanCounts",fanCounts);
        return Result.success(resultMap);
    }

    @PostMapping("/add")
    public Result AddFollow(@RequestHeader("Authorization") String token,@RequestBody Map<String,Object> followData){
        UserBean userBean = loginService.checkToken(token);
        if (userBean == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        Integer fanId = Integer.parseInt(followData.get("fanId").toString());
        Integer followId = Integer.parseInt(followData.get("followId").toString());
        Integer status = followService.AddFollow(fanId,followId);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }

    @PostMapping("/delete")
    public Result DeleteFollow(@RequestBody Map<String,Object> followData,@RequestHeader("Authorization") String token){
        UserBean userBean = loginService.checkToken(token);
        if (userBean == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        Integer fanId = Integer.parseInt(followData.get("fanId").toString());
        Integer followId = Integer.parseInt(followData.get("followId").toString());
        Integer status = followService.DeleteFollow(fanId,followId);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }
}
