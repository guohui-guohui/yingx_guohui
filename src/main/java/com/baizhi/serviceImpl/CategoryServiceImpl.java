package com.baizhi.serviceImpl;

import com.baizhi.annotation.AddLog;
import com.baizhi.dao.CategoryMapper;
import com.baizhi.entity.Category;
import com.baizhi.entity.CategoryExample;
import com.baizhi.service.CategoryService;
import com.baizhi.util.UUIDUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname CategoryServiceImpl
 * @Author GuOHuI
 * @Date 2020/12/22
 * @Time 19:15
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    HashMap<String, Object> map = new HashMap<>();

    @Override
    public HashMap<String, Object> queryOneCategory(Integer page, Integer rows) {
        // Integer page, Integer rows(每页展示条数)
        //返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数

        //设置当前页
         map.put("page",page);
        //创建查询条件
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(1);

        //创建分页对象  参数为 从第几条数据 ， 展示几条数据
        //                       offset                 展示rows条 limit
       RowBounds rowBounds = new RowBounds((page-1)*rows,rows);

        //查询数据

        //创建查询条件
        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);
        for(Category cate:categories){
           // System.out.println(cate.getId());
            CategoryExample example2 = new CategoryExample();
            example2.createCriteria().andParentIdEqualTo(cate.getId());
            int count = categoryMapper.selectCountByExample(example2);
            //System.out.println("count == "+count);
            cate.setCount(count);
        }
        map.put("rows",categories);

        //查询总条数
        int i = categoryMapper.selectCountByExample(example);
        map.put("records",i);

        //计算总页码
        Integer tol = i%rows == 0 ? i/rows:i/rows+1;
        map.put("total",tol);


        return map;
    }

    @Override
    public HashMap<String, Object> queryTwoCategory(Integer page, Integer rows, Category category) {
        // Integer page, Integer rows(每页展示条数)  category 存有父类别Id
        //返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数

        map.put("page",page);//当前页

        //创建查询条件
        CategoryExample example = new CategoryExample();
        example.createCriteria().andParentIdEqualTo(category.getParentId());
        //创建分页对象  参数为 从第几条数据 ， 展示几条数据
        //                                  offset       展示rows条 limit
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);

        //查询数据
        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows",categories);

        //查询总条数
        int i = categoryMapper.selectCountByExample(example);
        Category cate = new Category();
         cate.setCount(i);
        //System.out.println( "i 为 "+i);
        map.put("records",i);

        //计算总页码
        Integer tol = i%rows==0?i/rows:i/rows+1;
        map.put("total",tol);

        return map;
    }

    @AddLog(value = "添加类别信息")
    @Override
    public HashMap<String, Object> add(Category category) {
        String uuid = UUIDUtil.getUUID();
        category.setId(uuid);
        System.out.println(category);
        if(category.getParentId() == null){
            //一级类别
            category.setLevels(1);
            map.put("message","添加了一条一级类别的数据");
        }else {
            //二级类别
            category.setLevels(2);
            map.put("message","为"+category.getParentId()+" 添加了一条新数据");
        }
        categoryMapper.insertSelective(category);
        return map;
    }

    @AddLog(value = "修改类别信息")
    @Override
    public HashMap<String, Object> edit(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
        map.put("message","更新成功");
        return map;
    }

    @AddLog(value = "删除类别信息")
    @Override
    public HashMap<String, Object> del(Category category) {

        //删除时需要查询它的子表格数量，不为 0 的话不能是删除
        if (category.getParentId() == null){//不是子表格
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentIdEqualTo(category.getId());
            int count = categoryMapper.selectCountByExample(example);
            System.out.println("数据量  "+count);
            if (count == 0) {//数据量为0 可以删除
                categoryMapper.deleteByPrimaryKey(category);
                map.put("message","删除成功");
            }else{//存有数据不能删除
                //有二级类别  不能删除提示信息
                map.put("message","该一级类别下存有数据，不能删除！");
            }
        }else {//子表格
            categoryMapper.deleteByPrimaryKey(category);
            map.put("message","删除成功");
        }
        return map;
    }
}
