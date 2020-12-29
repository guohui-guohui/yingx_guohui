package com.baizhi.service;

import com.baizhi.entity.Category;

import java.util.HashMap;

/**
 * @Classname CategoryService
 * @Author GuOHuI
 * @Date 2020/12/22
 * @Time 19:14
 */
public interface CategoryService {

    HashMap<String,Object> queryOneCategory(Integer page, Integer rows);

    HashMap<String, Object> queryTwoCategory(Integer page, Integer rows, Category category);

    HashMap<String, Object> add(Category category);

    HashMap<String, Object> edit(Category category);

    HashMap<String, Object>  del(Category category);
}
