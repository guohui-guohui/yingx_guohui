package com.baizhi.serviceImpl;

import com.baizhi.annotation.AddLog;
import com.baizhi.dao.VideoMapper;
import com.baizhi.entity.UserExample;
import com.baizhi.entity.Video;
import com.baizhi.entity.VideoExample;
import com.baizhi.po.VideoPO;
import com.baizhi.po.VideoVO;
import com.baizhi.service.VideoService;
import com.baizhi.util.AliyunOSSUtil;
import com.baizhi.util.UUIDUtil;
import com.baizhi.util.VideoInterceptCoverUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
public class VideoServiceImpl implements VideoService {
    @Resource
    VideoMapper videoMapper;

    @Resource
    HttpSession session;
    @Resource
    HttpServletRequest request;

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
        List<Video> videos = videoMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows",videos);

        //查询总条数
        int i = videoMapper.selectCountByExample(example);
        map.put("records",i);

        //计算总页码
        Integer tol = i%rows==0?i/rows:i/rows+1;
        map.put("total",tol);

        return map;
    }

    @AddLog(value = "添加视频")
    @Override
    public String add(Video video) {
        String uuid = UUIDUtil.getUUID();

        video.setId(uuid);
        video.setUploadTime(new Date());

        //执行添加
        videoMapper.insertSelective(video);

        return uuid;
    }

    @AddLog(value = "修改视频")
    @Override
    public void edit(Video video) {

        if(video.getVideoPath() == ""){
            video.setVideoPath(null);
        }
        videoMapper.updateByPrimaryKeySelective(video);
    }

    @AddLog(value = "删除视频")
    @Override
    public void del(Video video) {
        //设置条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(video.getId());
        //根据id查询数据
        Video videos = videoMapper.selectOneByExample(example);

        //删除数据
        videoMapper.deleteByExample(example);
        String videoPath = videos.getVideoPath();

        //获取上传文件路径
        String realPath = request.getSession().getServletContext().getRealPath("/upload/video");

        //删除本地文件
        File file = new File(realPath + "/" + videoPath);

        //判断文件是否为一个文件并且存在
        if(file.isFile() && file.exists()){
            file.delete();
        }

    }

    /*
    * 上传到阿里云并截取视频帧也上传到阿里云   本地不参与
    * */
    @AddLog(value = "上传视频到阿里云并截取视频帧")
    @Override
    public void uploadVdieosAliyunPlus(MultipartFile videoPath, String id, HttpServletRequest request) {
        //获取文件名   a.mp4
        String filename = videoPath.getOriginalFilename();
        //拼接时间戳 创建新名字 234343-a.mp4
        String newName = new Date().getTime()+"-"+filename;
        //拼接视频名   video/234343-a.mp4 找到存放目录
        String objectName = "video/"+newName;

        /*上传至阿里云
         * 将文件上传至阿里云
         * 参数：
         *   headImg：MultipartFile类型的文件
         *   bucketName:存储空间名
         *   objectName:文件名
         * */
        AliyunOSSUtil.uploadBytesFile(videoPath,"yingx2006",objectName);
        //根据视频名拆分
        String[] split = newName.split("\\.");
        //拼接图片名
        String coverName = "cover/"+split[0]+".jpg";
        /*截取封面并上传
         * 视频截取帧并上传至阿里云
         * 参数：
         *   bucketName:存储空间名
         *   videoObjectName:视频文件名
         *   coverObjectName:封面文件名
         * */
        AliyunOSSUtil.videoInterceptCoverUpload("yingx2006",objectName,coverName);

        //修改数据
        //修改的条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        Video video = new Video();
        video.setCoverPath("http://yingx2006.oss-cn-beijing.aliyuncs.com/"+coverName); //设置封面
        video.setVideoPath("http://yingx2006.oss-cn-beijing.aliyuncs.com/"+objectName); //设置视频地址

        //修改
        videoMapper.updateByExampleSelective(video, example);
    }

    @AddLog(value = "删除视频数据")
    @Override
    public void deletePlus(Video video) {

        //  删除数据库内容
        //设置条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(video.getId());
        //根据id查询视频数据
        Video videos = videoMapper.selectOneByExample(example);

        //删除数据
        videoMapper.deleteByExample(example);

        //   删除阿里云内容
        /*
         * 删除文件  AliyunOSSUtil.deleteFile();
         * 参数：
         *   bucketName:存储空间名
         *   objectName:保存的图片名
         * */

        //.replace(str1,str2) 方法 是将str1 的内容 替换为 str2 的内容
        //http://yingx2006.oss-cn-beijing.aliyuncs.com/   video/1608781629917-动画.mp4
        //http://yingx2006.oss-cn-beijing.aliyuncs.com/   cover/1608781629917-动画.jpg

        //获取视频名字并拆分
        String videoName=videos.getVideoPath().replace("http://yingx2006.oss-cn-beijing.aliyuncs.com/","");
        //获取封面名字并拆分
        String coverName=videos.getCoverPath().replace("http://yingx2006.oss-cn-beijing.aliyuncs.com/","");

        //删除视频
        AliyunOSSUtil.deleteFile("yingx2006",videoName);
        //删除封面
        AliyunOSSUtil.deleteFile("yingx2006",coverName);
    }



    /*
    *
    * 上传到文件本地 问题 ： 占用内存
    * */
    @AddLog(value = "上传视频到本地")
    @Override
    public void uploadVdieos(MultipartFile videoPath, String id, HttpServletRequest request) {

        //获取文件上传的路径 根据相对路径获取绝对路劲
        String realPath = request.getSession().getServletContext().getRealPath("/upload/videos");

        //判断文件夹是否存在 不存在则创建
        File file = new File(realPath);
        if(!file.exists()){file.mkdirs();}

        //获取文件名
        String filename = videoPath.getOriginalFilename();
        //创建新名字  防止内容不同但文件名相同的文件受损
        String newName = new Date().getTime()+"-"+filename;

        //文件上传
        try {
            videoPath.transferTo(new File(realPath,newName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //修改图片路劲
            //修改条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        Video video = new Video();
        video.setCoverPath("gggg");//设置封面
        video.setVideoPath(newName);//视频地址

        //修改
        videoMapper.updateByExampleSelective(video,example);
    }

    /*
    * 上传到阿里云存储
    * */
    @AddLog(value = "上传视频并截取一帧")
    @Override
    public void uploadVdieosAliyun(MultipartFile videoPath, String id, HttpServletRequest request) {
        //上传到阿里云
        //获取文件名   XX.mp4
        String filename = videoPath.getOriginalFilename();
        //拼接时间戳  2341423424-XX.mp4
        String newName=new Date().getTime()+"-"+filename;
        //拼接视频名   video/2341423424-XX.mp4
        String objectName="video/"+newName;

        //System.out.println(objectName);
        /*1.上传至阿里云
         * 将文件上传至阿里云
         * 参数：
         *   headImg：MultipartFile类型的文件
         *   bucketName:存储空间名
         *   objectName:文件名
         * */
        AliyunOSSUtil.uploadBytesFile(videoPath,"yingx2006",objectName);


        //截取封面
        /**
         * 获取指定视频的帧并保存为图片至指定目录
         * @param videofile  源视频文件路径
         * @param framefile  截取帧的图片本地存放路径
         * @throws Exception
         */
        //http://z-yingx.oss-cn-beijing.aliyuncs.com/video/1608809719089-%E5%8A%A8%E7%94%BB.mp4?OSSAccessKeyId=LTAI4G7E6Gns2NsyNaWmZrCo&Expires=1608813386&Signature=BFyUPysis8ppxNxWzl5X2dA1rNs%3D
        //拼接视频文件路径              网络路径       +   视频名
        String videoNetPath = "http://yingx2006.oss-cn-beijing.aliyuncs.com/"+objectName;
        //System.out.println(videoNetPath);
        //截取后的文件存储的本地路径    根据相对路径获取绝对路径
        String realPath = request.getSession().getServletContext().getRealPath("/upload/cover");
        //判断文件夹是否存在
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();  //创建文件夹
        }

        //根据是视频名拆分     XXXXX.mp4
        String[] split = newName.split("\\.");
        //获取视频名字      XXXXX
        String splitName  = split[0];
        //拼接封面的本地路劲
        String coverLocalPath = realPath +"/"+splitName+".jpg";
        //System.out.println(coverLocalPath);
        try {
            VideoInterceptCoverUtil.fetchFrame(videoNetPath,coverLocalPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //上传封面
        /*
         * 3.上传封面
         * 参数：
         *   bucketName:存储空间名
         *   objectName:文件名
         *   localFilePath:本地文件路径
         * */
        //拼接封面网路经名
        String netCoverName="cover/"+splitName+".jpg";
        AliyunOSSUtil.uploadFile("yingx2006",netCoverName,coverLocalPath);

        //修改数据

        //删除本地文件
        File file1 = new File(coverLocalPath);
        //判断是一个文件，并且文件存在
        if(file1.isFile()&&file1.exists()){
            //删除文件
            boolean isDel = file1.delete();
            //System.out.println("删除："+isDel);
        }

        //修改数据
        //修改的条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        Video video = new Video();
        video.setCoverPath("http://yingx2006.oss-cn-beijing.aliyuncs.com/"+netCoverName); //设置封面
        video.setVideoPath("http://yingx2006.oss-cn-beijing.aliyuncs.com/"+objectName); //设置视频地址

        //修改
        videoMapper.updateByExampleSelective(video, example);
    }

    @Override
    public List<VideoVO> queryByReleaseTime() {

        List<VideoPO> videoPOList = videoMapper.queryByReleaseTime();
        ArrayList<VideoVO> videoVOs = new ArrayList<>();

        for (VideoPO videoPO : videoPOList) {
            
            //封装VO对象
            VideoVO videoVO = new VideoVO(videoPO.getId(),videoPO.getTitle(),videoPO.getCoverPath(),
                    videoPO.getCoverPath(),videoPO.getUploadTime(),videoPO.getDescription(),videoPO.getLikeCount(),
                    videoPO.getCateName(),videoPO.getUserPhoto()
                    );
            //将VO对象放入集合
            videoVOs.add(videoVO);

        }
        
        return videoVOs;
    }


}
