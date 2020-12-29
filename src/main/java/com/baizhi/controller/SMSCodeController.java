package com.baizhi.controller;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.baizhi.util.AliyunSendMsgUtil.getRandom;
import static com.baizhi.util.AliyunSendMsgUtil.sendPhoneCode;

/**
 * @Classname SMSCodeController
 * @Author GuOHuI
 * @Date 2020/12/23
 * @Time 9:27
 */
@RestController
@RequestMapping("/SMSCode")
public class SMSCodeController {

    @RequestMapping("/phoneCode")
    public String SMSCode(HttpSession session,String phoneNumbers){

        System.out.println("你输入的手机号为 _--_ "+phoneNumbers);
        //1.获取验证码随机数
        String random = getRandom(6);
        session.setAttribute("SMSCode",random);

        //存储验证码   session  redis
        System.out.println("验证码："+random);

        try {
            //2.发送验证码
            String message = sendPhoneCode(phoneNumbers, random);
            System.out.println(message);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return random;
    }
}
