package com.baizhi.serviceImpl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baizhi.annotation.AddLog;
import com.baizhi.dao.UserMapper;
import com.baizhi.entity.User;
import com.baizhi.entity.UserExample;
import com.baizhi.po.City;
import com.baizhi.po.CommonResult;
import com.baizhi.po.Pro;
import com.baizhi.po.UserVO;
import com.baizhi.service.UserService;
import com.baizhi.util.UUIDUtil;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @Classname UserServiceImpl
 * @Author GuOHuI
 * @Date 2020/12/21
 * @Time 16:18
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;


    @Override
    public HashMap<String, Object> queryUserPage(Integer page, Integer rows) {
        // Integer page, Integer rows(每页展示条数)
        //返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
        HashMap<String, Object> map = new HashMap<>();
        //设置当前页
        map.put("page",page);
        //创建查询条件
        UserExample example = new UserExample();
        //创建分页对象  参数为 从第几条数据 ， 展示几条数据
        //                       offset                 展示rows条 limit
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
        //查询数据
        List<User> users = userMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows",users);

        //查询总条数
        int i = userMapper.selectCountByExample(example);
        map.put("records",i);

        //计算总页码
        Integer tol = i%rows==0?i/rows:i/rows+1;
        map.put("total",tol);

        return map;
    }

    @AddLog(value = "添加用户信息")
    @Override
    public String add(User user) {
        String uuid = UUIDUtil.getUUID();
        user.setId(uuid);
        user.setCreateDate(new Date());
        user.setStatus("1");

        userMapper.insertSelective(user);

        //添加方法返回id
        return uuid;
    }

    @AddLog(value = "修改用户信息")
    @Override
    public void edit(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @AddLog(value = "删除用户信息")
    @Override
    public void del(User user) {
        userMapper.deleteByPrimaryKey(user);
    }


    /*文件上传*/
    @AddLog(value = "上传用户文件")
    @Override
    public void uploadUserCover(MultipartFile headImg, String id, HttpServletRequest request) {
        //1 获取文件名
        String filename = headImg.getOriginalFilename();

        String newName = new Date().getTime()+"-"+filename;//区分上传相同文件名时的问题

        //2 根据相对路径获取绝对路径
        String realPath = request.getServletContext().getRealPath("/bootstrap/img");

        //获取文件夹
        File file = new File(realPath);
        //判断文件夹存在不存在
        if(!file.exists()){file.mkdirs();/*创建文件夹 支持的是多级目录*/}



        //3 文件上传
        try {
            headImg.transferTo(new File(realPath,newName));
        } catch (IOException e) {
            throw new RuntimeException("文件上传出错");
        }

        User user = new User();
        user.setId(id);
        user.setHeadImg(newName);

        //4.修改数据
        userMapper.updateByPrimaryKeySelective(user);

    }

    @AddLog(value = "修改用户状态")
    @Override
    public User changeState(User user) {
        User one = userMapper.selectOneByExample(user);
        String status = one.getStatus();
        if(status.equals(user.getStatus()) && status.equals("1")){
            one.setStatus("0");
            userMapper.updateByPrimaryKeySelective(one);
        }else{
            one.setStatus("1");
            userMapper.updateByPrimaryKeySelective(one);
        }

        User selectOneByExample = userMapper.selectOneByExample(user);

        return selectOneByExample;
    }

    @Override
    public void poi(String excelTitle,User user) {

        try {
            List<User> users = userMapper.selectAll();
            //是指导出参数                                 标题         工作表名字
            ExportParams exportParams = new ExportParams(excelTitle, excelTitle);
            //导出表格    参数 ：        要导出的参数  类对象      数据集合
            Workbook excel = ExcelExportUtil.exportExcel(exportParams, User.class, users);

            File savefile = new File("F:\\excel");
            if (!savefile.exists()) {
                savefile.mkdirs();
            }

            excel.write(new FileOutputStream("F:/excel/ExcelExportHasImgTest.exportCompanyImg.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public HashMap<String, Object> getPhoneCodeLoginAndRegister(HttpSession session, String phone,String imgCode) {
        System.out.println("你输入的手机号为 _--_ "+phone);

        HashMap<String, Object> map = new HashMap<>();

        UserExample example = new UserExample();
        example.createCriteria().andPhoneEqualTo(phone);

        CommonResult commonResult = new CommonResult();
        User user = userMapper.selectOneByExample(example);
        String phoneCode = (String) session.getAttribute("phoneCode");
        if(phoneCode.equals(imgCode)) {
            map.put("status",100);
            map.put("message","登录成功");
            if (user == null) {
                //注册用户
                User use = new User();
                use.setId(UUIDUtil.getUUID());
                use.setPhone(phone);
                use.setCreateDate(new Date());
                use.setStatus("1");
                userMapper.insertSelective(use);
                map.put("data",use);
            }else{
                //登录
                map.put("data",user);

            }
        }else{
            map.put("status",100);
            map.put("message","错误信息");
        }

        return map;
    }

    @Override
    public HashMap<String,Object> userData() {

        List<UserVO> list = new ArrayList<>();

        //存放月份 TreeSet 排序控制重复月份
        Set<String> set = new TreeSet<>();

        HashMap<String, Object> map = new HashMap<>();
        List<UserVO> boy =  userMapper.selectUserDataBoy();


        //存放 count数量  boys数据
        ArrayList<Integer> objectBoyCount = new ArrayList<>();
        String month = null;
        Integer countBoy = null;
        for (UserVO userVO : boy) {
             month = userVO.getMonth();
             set.add(month);
             countBoy = userVO.getCount();
             objectBoyCount.add(countBoy);
        }
        //男 数量
        map.put("boys",objectBoyCount);


        List<UserVO> girl = userMapper.selectUserDataGirl();

        //存放 count数量 girl数据
        ArrayList<Integer> objectGirlCount = new ArrayList<>();
        Integer countGirls = null;
        for (UserVO userVO : girl) {
            month = userVO.getMonth();
            set.add(month);
            countGirls = userVO.getCount();
            objectGirlCount.add(countGirls);
        }


        System.out.println(month);
        System.out.println("月份"+set);
        //女 数量
        map.put("girls",objectGirlCount);

        for (String s : set) {


        }

        map.put("month",set);


        return map;
    }

    @Override
    public ArrayList<Pro> userCity(){

        ArrayList<City> boysCityList = new ArrayList<>();
        List<City> cities = userMapper.selectUserCityBoy();
        for (City city : cities) {
            boysCityList.add(city);
        }

        ArrayList<City> girlsCityList = new ArrayList<>();
        List<City> cities1 = userMapper.selectUserCityGirl();
        for (City city : cities1) {
            girlsCityList.add(city);
        }

        ArrayList<Pro> pros = new ArrayList<>();
        pros.add(new Pro("男",boysCityList));
        pros.add(new Pro("女",girlsCityList));

        return pros;
    }
}
