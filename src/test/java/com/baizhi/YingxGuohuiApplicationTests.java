package com.baizhi;

import com.aliyuncs.exceptions.ClientException;
import com.baizhi.dao.AdminMapper;
import com.baizhi.dao.UserMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.AdminExample;
import com.baizhi.entity.User;
import com.baizhi.po.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.baizhi.util.AliyunSendMsgUtil.getRandom;
import static com.baizhi.util.AliyunSendMsgUtil.sendPhoneCode;

@SpringBootTest
@Slf4j
class YingxGuohuiApplicationTests {

    @Resource
    AdminMapper adminMapper;
    @Resource
    UserMapper userMapper;
    @Test
    void contextLoads() {
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andUsernameEqualTo("guohui");
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        admins.forEach(admin -> System.out.println(admin));
    }

    @Test
    void insert() {
        Admin admin = new Admin("2","郭辉","123456","1","fsfgs");
        adminMapper.insert(admin);

    }

    @Test
    void query() {
        String phoneNumbers="15559966629,13769787245"; //手机号

        //1.获取验证码随机数
        String random = getRandom(6);

        //存储验证码   session  redis
        System.out.println("验证码："+random);

        try {
            //2.发送验证码
            String message = sendPhoneCode(phoneNumbers, random);
            System.out.println(message);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void queryUser() {
        User user = new User();

        List<User> users = userMapper.selectAll();

        for (User use:users){
            System.out.println(use);
        }
    }

    @Test
    void TestHutool() {
        //存放月份 TreeSet 排序控制重复月份
        List<String> list = new ArrayList<>();
        String month = null;

        List<UserVO> userVOS = userMapper.selectUserDataBoy();
        for (UserVO userVO : userVOS) {
           month = userVO.getMonth();
           list.add(month);
        }

        List<UserVO> userVOS1 = userMapper.selectUserDataGirl();
        for (UserVO userVO : userVOS1) {
            month = userVO.getMonth();
            list.add(month);
        }

        System.out.println(list);

        Set<String> treeSet = new TreeSet<>();

        for (String s : list) {
            treeSet.add(s);
        }

        System.out.println("treeSet"+treeSet);
    }

}
