package com.zixin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <E> Result<E> success(E data){
        return new Result<>(0,"Successful",data);
    }
    public static Result success(){
        return new Result(0,"Successful",null);
    }
    public static Result error(String message){
        return new Result(1, message,null);
    }

}
