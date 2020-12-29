package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "yx_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Excel(name = "ID")
    private String id;

    @Excel(name = "手机号码")
    private String phone;

    @Excel(name = "姓名")
    private String username;

    @Column(name = "head_img")
    @Excel(name = "头像", type = 2 ,width = 40 , height = 20,imageType = 1)
    private String headImg;

    @Excel(name = "简介")
    private String brief;

    @Excel(name = "状态")
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  //设置接受日期格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_date")
    @Excel(name = "创建时间",exportFormat = "yyyy-MM-dd")
    private Date createDate;

    @Transient
    @ExcelIgnore
    private String score;

    @Excel(name = "性别")
    private String sex;


}