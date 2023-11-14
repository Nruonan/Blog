package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.annotation.SystemLog;
import com.nruonan.service.LinkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("/link")
public class LinkController {
    @Resource
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @SystemLog(businessName = "获取所有友链")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
