package com.example.travel.mapper;

import com.example.travel.bean.TravelPlaceBean;
import com.example.travel.bean.UserBean;
import com.example.travel.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    List<UserBean> getUserList();
    List<UserBean> checkUser(String userName, String userPassword);
    UserBean getUserByName(String userName);
    UserBean getUserByNid(Integer userNid);
    int addUser(UserBean user);
    int updateUser(UserBean user);
    int deleteUserByName(String userName);
    int deleteUserByNid(int userNid);

    List<TravelPlaceBean> FootPrint(Integer userId);

    Integer AddFootPrint(Integer userId, String cityName, String travelTime);

    int setIpAndAddr(String userIp, String userAddr,Integer userNid);

    List<UserBean> getUserListByName(String searchWord);

    List<UserBean> getUserListBySex(String userSex);

    List<UserBean> getUserListByAddr(String searchWord);

    List<Map<String, Object>> getUserAddr();

    List<Map<String, Object>> getUserToAddr();

    List<Map<String, Object>> getUserSexRate();

    List<Integer> getLoginCount(Integer year, Integer month, Integer day);

    List<Integer> getSignCount(Integer year, Integer month, Integer day);
}
