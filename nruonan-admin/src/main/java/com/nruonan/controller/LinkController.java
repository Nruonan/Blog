package com.nruonan.controller;

import com.nruonan.ResponseResult;
import com.nruonan.domain.dto.AddLinkDto;
import com.nruonan.domain.entity.Link;
import com.nruonan.domain.vo.LinkVo;
import com.nruonan.service.LinkService;
import com.nruonan.utils.BeanCopyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author Nruonan
 * @description
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Resource
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult linkList(Integer pageNum, Integer pageSize, Link link){
        return linkService.linkList(pageNum,pageSize,link);
    }
    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto linkDto){
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        linkService.save(link);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable("id")Long id){
        Link link = linkService.getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }
    @PutMapping("/changeLinkStatus")
    //②修改友链状态
    public ResponseResult changeLinkStatus(@RequestBody LinkVo linkVo){
        Link link = BeanCopyUtils.copyBean(linkVo, Link.class);
        linkService.updateById(link);
        return ResponseResult.okResult();
    }
    @PutMapping()
    public ResponseResult update(@RequestBody LinkVo linkVo){
        Link link = BeanCopyUtils.copyBean(linkVo, Link.class);
        linkService.updateById(link);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id")Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
