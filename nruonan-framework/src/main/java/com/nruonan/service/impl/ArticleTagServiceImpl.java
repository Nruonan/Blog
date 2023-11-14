package com.nruonan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.domain.entity.ArticleTag;
import com.nruonan.mapper.ArticleTagMapper;
import com.nruonan.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-11-12 16:05:47
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
