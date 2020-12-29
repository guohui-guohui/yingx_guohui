package com.baizhi.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname VideoVO
 * @Author GuOHuI
 * @Date 2020/12/27
 * @Time 17:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVO {


    /*前台数据
    *
    * "id": "a11282389568441fa166ebedef03e530",
            "videoTitle": "人民的名义",
            "cover": "http://q40vnlbog.bkt.clouddn.com/1578650041065_人民的名义.jpg",
            "path": "http://q3th1ypw9.bkt.clouddn.com/1578650041065_人民的名义.mp4",
            "uploadTime": "2020-01-30",
            "description": "人民的名义",  //视频数据
            "likeCount": 0,         //喜欢人数   视频
            "cateName": "Java",    //类别数据
            "userPhoto":"http://q40vnlbog.bkt.clouddn.com/1.jpg"  //用户数据
    *
    * */

    private String id;
    private String videoTitle;
    private String cover;
    private String path;
    private String uploadTime;
    private String description;
    private Integer likeCount;
    private String cateName;
    private String userPhoto;

}
