package com.baizhi.util;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AliyunOSSUtil {


    // Endpoint以杭州为例，其它Region请按实际情况填写。Region：存储地址
    private static String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    private static String accessKeyId = "LTAI4G7E6Gns2NsyNaWmZrCo";
    private static String accessKeySecret = "lYtxh4E5vulmHW2dbZ2LZfwLv47dbp";


    /*
     * 删除文件  deleteFile
     * 参数：
     *   bucketName:存储空间名
     *   objectName:保存的图片名
     * */
    public static void deleteFile(String bucketName,String objectName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();

    }

    /*
     * 上传网络图片。。。。文件
     * 参数：
     *   bucketName:存储空间名
     *   objectName:保存的图片名
     *   netFilePath:文件网络地址
     * */
    public static void uploadNetFile(String bucketName,String objectName,String netFilePath)  {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传网络流。
        InputStream inputStream = null;
        try {
            inputStream = new URL(netFilePath).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ossClient.putObject(bucketName, objectName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }


    /*
     * 视频截取帧并上传至阿里云
     * 参数：
     *   bucketName:存储空间名
     *   videoObjectName:视频文件名
     *   coverObjectName:封面文件名
     * */
    public static void videoInterceptCoverUpload(String bucketName,String videoObjectName,String coverObjectName){


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置视频截帧操作。
        String style = "video/snapshot,t_1000,f_jpg,w_800,h_600";
        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, videoObjectName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);

        // 上传网络流。
        InputStream inputStream = null;
        try {
            inputStream = new URL(signedUrl.toString()).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //上传图片
        ossClient.putObject(bucketName, coverObjectName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
    /*
     * 将文件上传至阿里云
     * 参数：
     *   headImg：MultipartFile类型的文件
     *   bucketName:存储空间名
     *   objectName:文件名
     * */
    public static void uploadBytesFile(MultipartFile headImg, String bucketName, String objectName){

        byte[] bytes =null;
        try {
            //将文件转为byte数组
            bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

        // 上传Byte数组。
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));

        // 关闭OSSClient。
        ossClient.shutdown();
    }


    //创建存储空间
    public static String createBucket(String bucketName){

        OSS ossClient = null;
        try{
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 创建存储空间。
            ossClient.createBucket(bucketName);

            return "创建存储空间成功";
        }catch (Exception e){
            return "创建存储空间失败";
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

    }

    //上传文件  本地文件
    /*
     * 将本地文件上传至阿里云
     * 参数：
     *   bucketName:存储空间名
     *   objectName:文件名
     *   localFilePath:本地文件路径
     * */
    public static String uploadFile(String bucketName,String objectName,String localFile){

        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(localFile));

            // 上传文件。
            ossClient.putObject(putObjectRequest);

            return "文件上传成功";
        }catch (Exception e){
            return "文件上传失败";
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

    }

    //上传文件  文件流
    public static String uploadFiles(String bucketName,String objectName,String localFile) throws FileNotFoundException {
        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            InputStream inputStream = new FileInputStream(localFile);
            ossClient.putObject(bucketName, objectName, inputStream);

            return "文件上传成功了";
        }catch (Exception e){
            return "文件上传失败，请重新上传";
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

    }

    // 上传网络流。
    public static String uploadNetFile(String bucketName,String objectName) throws IOException {
        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传网络流。
            InputStream inputStream = new URL("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3860576747,4222045237&fm=26&gp=0.jpg").openStream();
            ossClient.putObject(bucketName, objectName, inputStream);

            return "网络资源上传成功";
        }catch (Exception e){
            return "网络资源上传失败";
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }


    }

    //下载文件到本地
    public static String dowloadFile(String bucketName,String objectName,String localFile) throws IOException {
        OSS ossClient = null;

        try{
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
            ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(localFile));

            return "下传成功";
        }catch (Exception e){
            return "下载失败";
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }



    }


    public static String showBucket(){
        OSS ossClient = null;
        try{
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 列举存储空间。
            List<Bucket> buckets = ossClient.listBuckets();
            for (Bucket bucket : buckets) {
                System.out.println(" - " + bucket.getName());
            }

            return "展示完毕";
        }catch (Exception e){
            return "出错了";
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

    }


    public static String deleteBucket(String bucketName){
        OSS ossClient = null;
        try{
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 删除存储空间。
            ossClient.deleteBucket(bucketName);

            return "删除成功";
        }catch (Exception e){
            return "删除失败";
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

    }






    public static void main(String[] args) {
        String name = "z-yingx";
        String s = AliyunOSSUtil.showBucket();
        System.out.println(s);
        //String createBucket = AliyunOSSUtil.createBucket(name);
       // System.out.println(createBucket);
    }
}
