package com.example.travel.controller;

import com.alibaba.fastjson.JSON;
import com.example.travel.bean.UserBean;
import com.example.travel.service.UserService;
import com.example.travel.utils.JWTUtils;
import com.example.travel.vo.ErrorCode;
import com.example.travel.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

import static com.example.travel.controller.UserController.checkUserBean;

/**
 * @author 杨乐乐
 * &#064;time  2023-03-28 15:05
 */
@RestController
/**
 * 前台注册与后台添加
 * @return  Token
 */
public class RegisterController {
    @Autowired
    UserService userService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public Result Register(@RequestBody UserBean NewUser) {
//        System.out.println(userBean1);
        UserBean userBean = userService.getUserByName(NewUser.getUserName());
        if(userBean != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        if(!checkUserBean(NewUser,true)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), "注册失败");
        }
//        System.out.println(NewUser);
        if(userService.addUser(NewUser)==0){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), "注册失败");
        }
        UserBean userBean1 = userService.getUserByName(NewUser.getUserName());
        String token = JWTUtils.createToken(userBean1.getUserNid());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(userBean1),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
