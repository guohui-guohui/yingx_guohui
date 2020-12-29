package com.baizhi.serviceImpl;

import com.baizhi.annotation.AddLog;
import com.baizhi.dao.AdminMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.AdminExample;
import com.baizhi.service.AdminService;
import com.baizhi.util.Md5Utils;
import com.baizhi.util.UUIDUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname UserServiceImpl
 * @Author GuOHuI
 * @Date 2020/12/20
 * @Time 14:01
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Resource
    AdminMapper adminMapper;

    @Resource
    HttpSession session;

    @Override
    public Admin login(Admin admin) {
        AdminExample example = new AdminExample();
        example.createCriteria().andUsernameEqualTo(admin.getUsername());

        Admin admins = adminMapper.selectOneByExample(example);

        return admins;
    }

    @Override
    public HashMap<String, Object> loginSer(Admin admin, String enCode) {
        //获取验证码
        String code = (String) session.getAttribute("code");

        HashMap<String, Object> map = new HashMap<>();

        //验证码判断
        if(enCode.equals(code)){
            //根据用户名查询用户数据
            AdminExample example = new AdminExample();
            example.createCriteria().andUsernameEqualTo(admin.getUsername());

            Admin admins = adminMapper.selectOneByExample(example);

            //判断用户是否存在
            if(admins!=null){
                //判断用户状态
                if(admins.getStatus().equals("0") ||admins.getStatus().equals("1")){//管理员登录
                    //加密
                    String md5Code = Md5Utils.getMd5Code(admins.getSalt() + admin.getPassword() + admins.getSalt());

                    //密码对比
                    if(admins.getPassword().equals(md5Code)){

                        //存储用户标记
                        session.setAttribute("admin",admins);

                        map.put("status","200");
                        map.put("message","登录成功");
                    }else{
                        map.put("status","401");
                        map.put("message","密码不正确");
                    }
                }else{
                    map.put("status","401");
                    map.put("message","用户权限不支持");
                }
            }else{
                map.put("status","401");
                map.put("message","该用户不存在");
            }
        }else{
            //验证码错误
            map.put("status","401");
            map.put("message","验证码错误");
        }

        return map;
    }

    @Override
    public HashMap<String, Object> queryUserPage(Integer page, Integer rows) {
        // Integer page, Integer rows(每页展示条数)
        //返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
        HashMap<String, Object> map = new HashMap<>();
        //设置当前页
        map.put("page",page);

        //创建查询条件
        AdminExample example = new AdminExample();
        //创建分页对象  参数为 从第几条数据 ， 展示几条数据
        //                                   offset       展示rows条 limit
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);

        //查询数据
        List<Admin> admins = adminMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows",admins);

        //查询总条数
        int i = adminMapper.selectCountByExample(example);
        map.put("records",i);

        //计算总页码
        Integer total = i%rows == 0 ? i/rows:i/rows+1;
        map.put("total",total);

        return map;
    }

    @AddLog(value = "添加管理员")
    @Override
    public void add(Admin admin) {
        //System.out.println("add 方法调用");
        String uuid = UUIDUtil.getUUID();
        //密码加密
        //生成随机盐
        String salt = Md5Utils.getSalt(6);
        //拼接随机盐加密
        String md5Code = Md5Utils.getMd5Code(salt + admin.getPassword() + salt);

        admin.setId(uuid);
        admin.setStatus("1");
        admin.setSalt(salt);
        admin.setPassword(md5Code);
        adminMapper.insertSelective(admin);
    }

    @AddLog(value = "修改管理员信息")
    @Override
    public void edit(Admin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    @AddLog(value = "删除管理员")
    @Override
    public void del(Admin admin) {
        //System.out.println("del 方法调用");
        adminMapper.deleteByPrimaryKey(admin);
    }
}
