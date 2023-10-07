package com.example.travel.service;

import com.example.travel.bean.TravelPlaceBean;
import com.example.travel.bean.UserBean;
import com.example.travel.mapper.UserMapper;
import com.example.travel.utils.IpUtils;
import com.example.travel.vo.ErrorCode;
import com.example.travel.vo.Result;
import com.example.travel.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginService loginService;

    public List<UserBean> getUserList() {
        return userMapper.getUserList();
    }

    public UserBean checkUser(String userName, String userPassword) {
        List<UserBean> userList = userMapper.checkUser(userName, userPassword);
        return userList.isEmpty() ? null : userList.get(0);
    }
    public Result getUserByToken(String token){
        /**
         * 1. token合法性校验
         *    是否为空，解析是否成功 redis是否存在
         * 2. 如果校验失败 返回错误
         * 3. 如果成功，返回对应的结果 LoginUserVo
         */
            UserBean sysUser = loginService.checkToken(token);
            if (sysUser == null){
                return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
            }
            //获取用户ip并存入数据库

            String ipAddr = IpUtils.getIpAddress();
            //TODO:需要更改，暂时测试使用南京IP
            String addr = IpUtils.getAddress(ipAddr);
            if(Objects.equals(addr, "")){
                addr=sysUser.getUserAddr();
            }
            userMapper.setIpAndAddr(ipAddr,addr,sysUser.getUserNid());
            UserVo loginUser = new UserVo();
            loginUser.setUserNid(sysUser.getUserNid());
            loginUser.setUserName(sysUser.getUserName());
            loginUser.setUserAvatarUrl(sysUser.getUserAvatarUrl());
            loginUser.setUserAddr(addr);
            return Result.success(loginUser);//将赋完值的loginUserVo放在Result的success方法返回
        }
    public UserBean getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    public List<TravelPlaceBean> FootPrint(Integer id){
        return userMapper.FootPrint(id);
    }

    public  Integer AddFootPrint(Integer id, String cityName, String time){
        return userMapper.AddFootPrint(id,cityName,time);
    }
    public UserBean getUserByNid(Integer id) {
        return userMapper.getUserByNid(id);
    }

    public int addUser(UserBean user) {
        return userMapper.addUser(user);
    }

    public int updateUser(UserBean user) { return userMapper.updateUser(user); }

    public int deleteUserByName(String userName) { return userMapper.deleteUserByName(userName); }

    public int deleteUserByNid(int userNid) {
        return userMapper.deleteUserByNid(userNid);
    }

    public List<UserBean> getUserListByName(String searchWord) {
        return userMapper.getUserListByName(searchWord);
    }

    public List<UserBean> getUserListBySex(String searchWord) {
        return userMapper.getUserListBySex(searchWord);
    }

    public List<UserBean> getUserListByAddr(String searchWord) {
        return userMapper.getUserListByAddr(searchWord);
    }

    public List<Map<String, Object>> getUserAddr() {
        return userMapper.getUserAddr();
    }

    public List<Map<String, Object>> getUserToAddr() {
        return userMapper.getUserToAddr();
    }

    public List<Map<String, Object>> getUserSexRate() {
        return userMapper.getUserSexRate();
    }

    public Integer getLoginCount(Integer year, Integer month, Integer day) {
        return userMapper.getLoginCount(year,month,day).get(0);
    }

    public Integer getSignCount(Integer year, Integer month, Integer day) {
        return userMapper.getSignCount(year,month,day).get(0);
    }
}
