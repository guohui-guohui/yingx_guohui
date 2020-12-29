package com.baizhi.service;

import com.baizhi.entity.Admin;

import java.util.HashMap;

/**
 * @Classname UserService
 * @Author GuOHuI
 * @Date 2020/12/20
 * @Time 14:01
 */
public interface AdminService {

    Admin login(Admin admin);

    HashMap<String, Object> loginSer(Admin admin, String enCode);

    HashMap<String, Object> queryUserPage(Integer page, Integer rows);

    void add(Admin admin);

    void edit(Admin admin);

    void del(Admin admin);
}
