package com.nruonan.service.impl;


import com.google.gson.Gson;
import com.nruonan.ResponseResult;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.handler.exception.SystemException;
import com.nruonan.service.UploadService;
import com.nruonan.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author Nruonan
 * @description
 */
@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class UploadServiceImpl implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) throws SystemException {
        //判断文件类型
        // 获取原始文件名
        String originalFilename = img.getOriginalFilename();
        if (!originalFilename.endsWith(".png") ) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);

        }
        // 判断通过 上传文件到oss
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(img,filePath);    //2023/11/6/wq.png
        return ResponseResult.okResult(url);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    private String uploadOss(MultipartFile imgFile,String filePath){
        //构造一个带指定 Region 对象的配置类。你的七牛云OSS创建的是哪个区域的，那么就调用Region的什么方法即可
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);


        String key = filePath;

        try {

            InputStream inputStream = imgFile.getInputStream();

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("上传成功! 生成的key是: "+putRet.key);
                System.out.println("上传成功! 生成的hash是: "+putRet.hash);
                return "http://s3smg5yb5.hn-bkt.clouddn.com/" + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        }catch (Exception e) {
            //ignore
        }
        return "ww";
    }
}
