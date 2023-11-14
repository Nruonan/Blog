package com.nruonan.service;

import com.nruonan.ResponseResult;
import com.nruonan.handler.exception.SystemException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Nruonan
 * @description
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img) throws SystemException;
}
