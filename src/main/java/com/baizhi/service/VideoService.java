package com.baizhi.service;

import com.baizhi.entity.Video;
import com.baizhi.po.VideoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname UserService
 * @Author GuOHuI
 * @Date 2020/12/21
 * @Time 16:18
 */
public interface VideoService {

    HashMap<String, Object> queryUserPage(Integer page, Integer rows);

    String add(Video video);

    void edit(Video video);

    void del(Video video);

    void uploadVdieosAliyun(MultipartFile videoPath, String id, HttpServletRequest request);

    void uploadVdieos(MultipartFile videoPath, String id, HttpServletRequest request);

    void uploadVdieosAliyunPlus(MultipartFile videoPath, String id, HttpServletRequest request);

    void deletePlus(Video video);

    List<VideoVO> queryByReleaseTime();
}
