package com.baizhi.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @Classname Pro
 * @Author GuOHuI
 * @Date 2020/12/28
 * @Time 21:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pro {

    private String sex;
    private ArrayList<City> citys;

}