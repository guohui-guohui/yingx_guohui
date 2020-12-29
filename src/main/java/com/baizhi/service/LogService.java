package com.baizhi.service;

import java.util.HashMap;

/**
 * @Classname UserService
 * @Author GuOHuI
 * @Date 2020/12/21
 * @Time 16:18
 */
public interface LogService {

    HashMap<String, Object> queryLogPage(Integer page, Integer rows);

}
