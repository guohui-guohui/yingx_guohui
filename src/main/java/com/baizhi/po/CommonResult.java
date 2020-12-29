package com.baizhi.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname CommonResult
 * @Author GuOHuI
 * @Date 2020/12/27
 * @Time 17:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult {

    private String message;
    private String status;
    private Object data;

    public CommonResult success(String message,String status,Object data){
        CommonResult commoResult = new CommonResult();
        commoResult.setMessage(message);
        commoResult.setStatus(status);
        commoResult.setData(data);
        return commoResult;
    }


    public CommonResult success(String status,Object data){
        CommonResult commoResult = new CommonResult();
        commoResult.setMessage("查询成功");
        commoResult.setStatus(status);
        commoResult.setData(data);
        return commoResult;
    }

    public CommonResult success(Object data){
        CommonResult commoResult = new CommonResult();
        commoResult.setMessage("查询成功");
        commoResult.setStatus("100");
        commoResult.setData(data);
        return commoResult;
    }


    public CommonResult filed(String message,String status,Object data){
        CommonResult commoResult = new CommonResult();
        commoResult.setMessage(message);
        commoResult.setStatus(status);
        commoResult.setData(data);
        return commoResult;
    }

    public CommonResult filed(String status,Object data){
        CommonResult commoResult = new CommonResult();
        commoResult.setMessage("查询失败");
        commoResult.setStatus(status);
        commoResult.setData(data);
        return commoResult;
    }

    public CommonResult filed(Object data){
        CommonResult commoResult = new CommonResult();
        commoResult.setMessage("查询失败");
        commoResult.setStatus("104");
        commoResult.setData(data);
        return commoResult;
    }

    public CommonResult filed(){
        CommonResult commoResult = new CommonResult();
        commoResult.setMessage("查询失败");
        commoResult.setStatus("104");
        commoResult.setData(null);
        return commoResult;
    }


}