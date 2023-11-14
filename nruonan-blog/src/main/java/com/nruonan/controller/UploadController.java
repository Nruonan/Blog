package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.annotation.SystemLog;
import com.nruonan.handler.exception.SystemException;
import com.nruonan.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 * @author Nruonan
 * @description
 */
@RestController
public class UploadController {
    @Resource
    private UploadService uploadService;

    @PostMapping("/upload")
    @SystemLog(businessName = "上传图片")
    public ResponseResult uploadImg(MultipartFile img) throws SystemException {
        return uploadService.uploadImg(img);
    }
}


