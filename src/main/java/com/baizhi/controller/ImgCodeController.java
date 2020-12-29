package com.baizhi.controller;

import com.baizhi.util.CreateValidateCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Classname ImgCode
 * @Author GuOHuI
 * @Date 2020/12/20
 * @Time 12:49
 */
//验证码

@Controller
@RequestMapping("/Img")
public class ImgCodeController {

    @RequestMapping("/code")
    public String code(HttpServletResponse response, HttpSession session) throws IOException {
        CreateValidateCode code = new CreateValidateCode(100,30,6,10);
        session.setAttribute("code",code.getCode());
        code.write(response.getOutputStream());
        return null;
    }

}
