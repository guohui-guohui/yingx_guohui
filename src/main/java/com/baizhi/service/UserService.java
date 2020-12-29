package com.baizhi.service;

import com.baizhi.entity.User;
import com.baizhi.po.Pro;
import com.baizhi.po.UserVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Classname UserService
 * @Author GuOHuI
 * @Date 2020/12/21
 * @Time 16:18
 */
public interface UserService {

    HashMap<String, Object> queryUserPage(Integer page, Integer rows);

    String add(User user);

    void uploadUserCover(MultipartFile headImg, String id, HttpServletRequest request);

    void edit(User user);

    void del(User user);

    User changeState(User user);

    void poi(String excelTitle,User user);

    public HashMap<String, Object> getPhoneCodeLoginAndRegister(HttpSession session, String phone,String imgCode);

    HashMap<String,Object> userData();

    ArrayList<Pro> userCity();
}
