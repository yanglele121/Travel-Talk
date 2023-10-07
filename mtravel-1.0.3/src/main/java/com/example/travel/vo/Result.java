package com.example.travel.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * @author 杨乐乐
 * @time 2023-03-28 14:38
 */
//所有接口返回值格式
@Data
@AllArgsConstructor
public class Result {
    private boolean success;

    private int code;

    private String msg;

    private Object data;


    public static Result success(Object data){
        return new Result(true,200,"success",data);
    }

    public static Result fail(int code, String msg){
        return new Result(false,code,msg,null);
    }
}
