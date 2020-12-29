package com.baizhi.serviceImpl;

import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Log;
import com.baizhi.entity.UserExample;
import com.baizhi.service.LogService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname UserServiceImpl
 * @Author GuOHuI
 * @Date 2020/12/21
 * @Time 16:18
 */
@Transactional
@Service
public class LogServiceImpl implements LogService {
    @Resource
    LogMapper logMapper;


    @Override
    public HashMap<String, Object> queryLogPage(Integer page, Integer rows) {
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
        List<Log> logs = logMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows",logs);

        //查询总条数
        int i = logMapper.selectCountByExample(example);
        map.put("records",i);

        //计算总页码
        Integer tol = i%rows==0?i/rows:i/rows+1;
        map.put("total",tol);

        return map;
    }

}
