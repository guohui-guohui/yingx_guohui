package com.baizhi.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname VideoPO
 * @Author GuOHuI
 * @Date 2020/12/27
 * @Time 17:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoPO {

    /*
    *数据库别名数据
    *
    * */
    private String id;
    private String title;
    private String description;
    private String videoPath;
    private String coverPath;
    private String uploadTime;
    private String categoryId;
    private String groupId;
    private String userId;
    private String userPhoto;
    private String cateName;
    private Integer likeCount;
}
