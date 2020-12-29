package com.baizhi.controller;

import com.baizhi.entity.Admin;
import com.baizhi.entity.Category;
import com.baizhi.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Classname CategoryController
 * @Author GuOHuI
 * @Date 2020/12/22
 * @Time 19:15
 */
@RequestMapping("/category")
@Controller
public class CategoryController {

    @Resource
    CategoryService categoryService;

    //一级类别
    //接收  page=当前页   rows=每页展示条数   返回  page=当前页   rows=[User,User]数据    tolal=总页数 records=总条数
    @ResponseBody
    @RequestMapping(value = "/queryOneCategory")
    public HashMap<String ,Object> queryOneCategory(Integer page,Integer rows){
        return categoryService.queryOneCategory(page, rows);
    }

    //查询二级类别
    @ResponseBody
    @RequestMapping(value = "/queryTwoCategory")
    public HashMap<String ,Object> queryTwoCategory(Integer page, Integer rows, Category category){
        return categoryService.queryTwoCategory(page, rows,category);
    }

    @ResponseBody
    @RequestMapping("/edit")
    public HashMap<String, Object> edit(Category category, String oper) {
        HashMap<String, Object> map = null;
        if (oper.equals("add")) {
            //System.out.println(category);
            map = categoryService.add(category);
        }
        if (oper.equals("edit")) {
            map = categoryService.edit(category);
        }
        if (oper.equals("del")) {
            map = categoryService.del(category);
        }
        return map;
    }
}
