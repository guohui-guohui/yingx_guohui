package com.baizhi.controller;

import com.baizhi.annotation.AddLog;
import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @Classname UserController
 * @Author GuOHuI
 * @Date 2020/12/20
 * @Time 13:58
 */


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    //登录
    @ResponseBody
    @RequestMapping("/login")
    public HashMap<String, Object> login(Admin admin, String enCode){
        return adminService.loginSer(admin, enCode);
    }

    @RequestMapping("/loging")
    public String loging(Admin admin, String enCode, HttpSession session){
        Admin user = adminService.login(admin);

        //获取Session作用域中的验证码
        String  code = (String) session.getAttribute("code");

        //登录判断
        if(user != null) {//用户存在
            if (!code.equals(enCode)) {//验证码输入错误
                session.setAttribute("mesg", "验证码错误");
                return  "redirect:/login/login.jsp";
            }else{//验证码正确,验证密码信息

                if(!user.getPassword().equals(admin.getPassword())){//密码不正确
                    session.setAttribute("mesg","密码输入有误");
                    return  "redirect:/login/login.jsp";
                }else{//信息正确，登录成功
                    session.setAttribute("admin",user);
                    return  "main/main";
                }

            }
        }else {
            //用户不存在
            session.setAttribute("mesg","用户不存在，请重新输入...");
            return  "redirect:/login/login.jsp";
        }
    }

    //安全退出
    @RequestMapping("/Exit")
    public String out(HttpSession session){
        session.removeAttribute("admin");
        session.invalidate();
        return "redirect:/login/login.jsp";

    }


    @ResponseBody
    @RequestMapping(value = "/queryAdminPage")
    public HashMap<String, Object> queryAdminPage(Integer page, Integer rows){
        return adminService.queryUserPage(page,rows);
    }

    @ResponseBody
    @RequestMapping("/edit")
    public void edit(Admin admin, String oper) {
        //System.out.println("用户为 "+admin);
        if (oper.equals("add")) {
            //System.out.println("add 方法调用");
            adminService.add(admin);
        }
        if (oper.equals("edit")) {
            adminService.edit(admin);
        }
        if (oper.equals("del")) {
            System.out.println("del 方法调用");
            adminService.del(admin);
        }
    }
}
