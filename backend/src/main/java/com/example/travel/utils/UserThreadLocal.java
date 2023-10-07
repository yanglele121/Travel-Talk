package com.example.travel.utils;

import com.example.travel.bean.UserBean;

public class UserThreadLocal {

    private UserThreadLocal(){}
    //线程变量隔离
    private static final ThreadLocal<UserBean> LOCAL = new ThreadLocal<>();

    public static void put(UserBean sysUser){
        LOCAL.set(sysUser);
    }

    public static UserBean get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
