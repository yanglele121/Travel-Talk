package com.example.travel.service;

import com.alibaba.fastjson.JSON;
import com.example.travel.bean.AdminBean;
import com.example.travel.bean.UserBean;
import com.example.travel.utils.JWTUtils;
import com.example.travel.vo.ErrorCode;
import com.example.travel.vo.Result;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 杨乐乐
 * @time 2023-03-28 20:57
 */
@Service
public class LoginService {
    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public Result login(Map<String, Object> params){
        String userName = params.get("userName").toString();
        String userPassword = params.get("userPassword").toString();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userPassword)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        UserBean userBean= userService.checkUser(userName,userPassword);
        if (userBean == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        userBean.setLastLogin(LocalDateTime.now());
        userService.updateUser(userBean);
        String token = JWTUtils.createToken(userBean.getUserNid());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(userBean),1, TimeUnit.DAYS);
        return Result.success(token);
    }
    public UserBean checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            System.out.println("token已经过期");
            return null;
        }
        return JSON.parseObject(userJson, UserBean.class);
    }

    public Result adminLogin(Map<String, Object> params) {
        String userName = params.get("username").toString();
        String userPassword = params.get("password").toString();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userPassword)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        AdminBean adminBean =adminService.checkAdmin(userName,userPassword);
        if (adminBean == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(adminBean.getAdminNid());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(adminBean),3, TimeUnit.HOURS);
        return Result.success(token);
    }

    public AdminBean checkAdminToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            System.out.println("token已经过期");
            return null;
        }
        return JSON.parseObject(userJson, AdminBean.class);
    }

    public void logout(String token){
        redisTemplate.delete("TOKEN_"+token);
    }
}
