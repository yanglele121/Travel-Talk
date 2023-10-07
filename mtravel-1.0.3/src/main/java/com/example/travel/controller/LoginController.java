package com.example.travel.controller;

import com.example.travel.bean.AdminBean;
import com.example.travel.service.LoginService;
import com.example.travel.vo.ErrorCode;
import com.example.travel.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public Result loginCheck(@RequestBody Map<String, Object> params) {
        return loginService.login(params);
    }

    @RequestMapping(value = "/admin/login",method = RequestMethod.POST)
    public Result AdminCheck(@RequestBody Map<String,Object> params){
        return loginService.adminLogin(params);
    }

    @GetMapping("/admin/logout")
    public Result AdminLogOut(@RequestHeader("Authorization") String token){
        loginService.logout(token);
        return Result.success(null);
    }
    @GetMapping("/logout")
    public Result Logout(@RequestHeader("Authorization") String token){
        loginService.logout(token);
        return Result.success(null);
    }

    @GetMapping("/admin/curAdmin")
    public Result curAdmin(@RequestHeader("Authorization") String token) {
        System.out.println("Token1111 " + token);
        if (token == null) {
            System.out.println(111);
            return Result.success(null);
        } else {
            System.out.println("Token " + token);
            AdminBean adminBean = loginService.checkAdminToken(token);
            System.out.println(adminBean);
            if (adminBean == null) {
                return Result.fail(5006, ErrorCode.NO_LOGIN.getMsg());
            }
            return Result.success(adminBean);
        }
    }

}
