package com.baizhi.app;

import com.aliyuncs.exceptions.ClientException;
import com.baizhi.po.CommonResult;
import com.baizhi.po.VideoVO;
import com.baizhi.service.UserService;
import com.baizhi.service.VideoService;
import com.baizhi.util.AliyunSendMsgUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

import static com.baizhi.util.AliyunSendMsgUtil.getRandom;
import static com.baizhi.util.AliyunSendMsgUtil.sendPhoneCode;

/**
 * @Classname InterfaceController
 * @Author GuOHuI
 * @Date 2020/12/27
 * @Time 16:42
 */

/*
*
* 前台接口
* */
@RestController
@CrossOrigin
@RequestMapping("/app")
public class InterfaceController {
    @Resource
    VideoService videoService;
    @Resource
    UserService userService;

    @RequestMapping("/getPhoneCode")
    public HashMap<String, Object> getPhoneCode(String phone,HttpSession session){

        HashMap<String, Object> map = new HashMap<>();

        //发送手机验证码
        String message=null;
        try {
            String random = AliyunSendMsgUtil.getRandom(6);
            session.setAttribute("phoneCode",random);
            message = AliyunSendMsgUtil.sendPhoneCode(phone, random);
            map.put("status","100");
            map.put("data",phone);
            map.put("message","验证码发送成功");
        } catch (ClientException e) {
            e.printStackTrace();
            map.put("status","100");
            map.put("data",null);
            map.put("message","验证码发送失败");
        }


        return map;
    }

    @RequestMapping("/queryByReleaseTime")
    public CommonResult queryByReleaseTime(){
        try {
            List<VideoVO> videoVOS = videoService.queryByReleaseTime();
            return new CommonResult().success(videoVOS);
        } catch (Exception e) {
            return new CommonResult().filed();
        }
    }

    //前台用户登录
    @RequestMapping("/login")
    public HashMap<String, Object> getPhoneCodeLoginAndRegister(HttpSession session, String phone, String imgCode){
        return userService.getPhoneCodeLoginAndRegister(session, phone, imgCode);

    }
}
