package com.baizhi.dao;

import com.baizhi.entity.Video;
import com.baizhi.entity.VideoExample;
import java.util.List;

import com.baizhi.po.VideoPO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface VideoMapper extends Mapper<Video> {
    List<VideoPO> queryByReleaseTime();
}