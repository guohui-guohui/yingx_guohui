package com.baizhi.dao;

import com.baizhi.entity.User;
import com.baizhi.entity.UserExample;
import java.util.List;

import com.baizhi.po.City;
import com.baizhi.po.Pro;
import com.baizhi.po.UserVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    List<UserVO> selectUserDataBoy();

    List<UserVO> selectUserDataGirl();

    List<City> selectUserCityBoy();

    List<City> selectUserCityGirl();
}