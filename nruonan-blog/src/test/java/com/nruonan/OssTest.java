package com.nruonan;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * @author Nruonan
 * @description
 */
@ConfigurationProperties(prefix = "oss")//指定读取application.yml文件的oss属性的数据
@SpringBootTest
@Component
public class OssTest {
    //注意要从application.yml读取属性数据，下面的3个成员变量的名字必须对应application.yml的myoss属性的三个子属性名字
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

    @Test
    public void testOss(){
        //构造一个带指定 Region 对象的配置类。你的七牛云OSS创建的是哪个区域的，那么就调用Region的什么方法即可
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);

        //打开七牛云，把鼠标悬浮在右上角的个人头像，然后就会看到'密钥管理'，点击进入就有你的密钥，把其中的AK和SK复制到下面两行
       /* String accessKey = "wyEqV9aSxOZU0wRnDKWJw_zRqAwXRV47EeCmIrtB";
        String secretKey = "7rgUg4h3JXSnDMV8VNH2oR5K6zREX4POeOgU7OYC";
        String bucket = "nnn-blog";*/
        //为避免上面3行暴露信息，我们会把信息写到application.yml里面，然后添加ConfigurationProperties注解、3个成员变量即可读取

        //文件名，如果写成null的话，就以文件内容的hash值作为文件名
        String key = null;

        try {

            //byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            //ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);

            //上面两行是官方写的(注释掉)，下面那几行是我们写的
            InputStream inputStream = new FileInputStream("d:\\用户\\Pictures\\R-C.png");

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("上传成功! 生成的key是: "+putRet.key);
                System.out.println("上传成功! 生成的hash是: "+putRet.hash);
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
    }
}
