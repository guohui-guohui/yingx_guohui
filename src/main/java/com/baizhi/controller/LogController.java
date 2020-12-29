package com.baizhi.controller;

import com.baizhi.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Classname UserController
 * @Author GuOHuI
 * @Date 2020/12/21
 * @Time 16:01
 */
@RequestMapping("/log")
@Controller
@Slf4j
public class LogController {

    @Resource
    LogService logService;

    @ResponseBody
    @RequestMapping(value = "/queryLogPage")
    public HashMap<String, Object> queryLogPage(Integer page, Integer rows){

        return logService.queryLogPage(page,rows);
    }

}
