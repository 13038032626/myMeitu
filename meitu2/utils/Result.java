package com.example.meitu2.utils;

import lombok.Data;

@Data
public class Result {

    String msg;

    Object data;

    public Result(String s,Object obj){
        this.msg = s;
        this.data = obj;
    }

    public static Result ok(Object obj){
        Result success = new Result("success", obj);
        return success;
    }

    public static  Result error(){
        return new Result("error",null);
    }


}

