package com.baizhi.controller;

import com.alibaba.fastjson.JSON;
import com.baizhi.entity.User;
import com.baizhi.po.Pro;
import com.baizhi.service.UserService;
import io.goeasy.GoEasy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Classname UserController
 * @Author GuOHuI
 * @Date 2020/12/21
 * @Time 16:01
 */
@RequestMapping("/user")
@Controller
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/getUserChina")
    public String getUserChina(){
        ArrayList<Pro> pros = userService.userCity();
        //System.out.println(map);
        //将Map对象转为json字符串
        String content = JSON.toJSONString(pros);

        /*创建GoEasy对象并设置参数
         * 参数：
         *  应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
         *  Appkey
         * */
        GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-bc9baffe713945f18c8df013e5acff44");
        //发布消息 参数：通道名称，要发送消息
        goEasy.publish("channel",content);
        System.out.println("---------"+content);
        return content;
    }


    @ResponseBody
    @RequestMapping(value = "/getUserData")
    public String getUserData(){

        HashMap<String, Object> map = userService.userData();
        //System.out.println(map);
        //将Map对象转为json字符串
        String content = JSON.toJSONString(map);

        /*创建GoEasy对象并设置参数
         * 参数：
         *  应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
         *  Appkey
         * */
        GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-bc9baffe713945f18c8df013e5acff44");
        //发布消息 参数：通道名称，要发送消息
        goEasy.publish("my2006_channel",content);
        System.out.println("---------"+content);
        return content;
    }

    @ResponseBody
    @RequestMapping(value = "/queryUserPage")
    public HashMap<String, Object> queryUserPage(Integer page, Integer rows){
        return userService.queryUserPage(page,rows);
    }

    @ResponseBody
    @RequestMapping(value = "/update")
    public void update(User user){
        log.info("状态为 "+user.getStatus());
        log.info("id 为 "+user.getId());
    }

    @ResponseBody
    @RequestMapping("/edit")
    public String edit(User user,String oper){
        //System.out.println("User 用户为"+ user);
        String id =null;
        if(oper.equals("add")){
            id = userService.add(user);
        }
        if(oper.equals("edit")){
            userService.edit(user);
            id = user.getId();
        }
        if(oper.equals("del")){
            userService.del(user);
        }
        return id;
    }

    @ResponseBody
    @RequestMapping("/uploadUserCover")
    public void uploadUserCover(MultipartFile headImg, String id, HttpServletRequest request){
        userService.uploadUserCover(headImg, id, request);
    }

    //状态更改
    @ResponseBody
    @RequestMapping("/changeState")
    public User changeState(User user){
        log.info("状态 + + + "+user.getStatus());
        return userService.changeState(user);
    }

    //导出Excel表格
    @ResponseBody
    @RequestMapping("/poi")
    public void poi(String excelTitle,User user){
        log.info("表格标题 + + + "+excelTitle);
        userService.poi(excelTitle,user);
    }
}
