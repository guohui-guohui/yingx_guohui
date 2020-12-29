package com.baizhi.controller;

import com.baizhi.entity.Video;
import com.baizhi.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @Classname UserController
 * @Author GuOHuI
 * @Date 2020/12/21
 * @Time 16:01
 */
@RequestMapping("/video")
@Controller
@Slf4j
public class VideoController {

    @Resource
    VideoService videoService;

    @ResponseBody
    @RequestMapping(value = "/queryVideoPage")
    public HashMap<String, Object> queryUserPage(Integer page, Integer rows){
        return videoService.queryUserPage(page,rows);
    }

    @ResponseBody
    @RequestMapping("/edit")
    public String edit(Video video, String oper){
        log.info("调用方法 "+oper);
        System.out.println("接收到的参数"+"\n" +video);
        String id =null;
        if(oper.equals("add")){
            id = videoService.add(video);
        }
        if(oper.equals("edit")){
            videoService.edit(video);
            id = video.getId();
        }
        if(oper.equals("del")){
            videoService.deletePlus(video);
        }
        return id;
    }

    @ResponseBody
    @RequestMapping("/uploadVdieo")
    public void uploadVdieo(MultipartFile videoPath, String id, HttpServletRequest request) {
        videoService.uploadVdieosAliyunPlus(videoPath, id, request);
    }


    @ResponseBody
    @RequestMapping("/searchVdieo")
    public void searchVdieo(String content) {

    }
}
