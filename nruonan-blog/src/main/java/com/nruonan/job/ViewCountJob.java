package com.nruonan.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.entity.Article;
import com.nruonan.mapper.ArticleMapper;
import com.nruonan.service.ArticleService;
import com.nruonan.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nruonan
 * @description
 */
@Component
public class ViewCountJob {
    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleService service;

    //每隔3分钟，把redis的浏览量数据更新到mysql数据库
    @Scheduled(cron = "* 0/10 * * * ?")
    public void updateViewCount() {
        //获取redis中浏览量数据
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT);

        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry ->
                        new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());

        for (Article article : articles) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article::getId, article.getId());
            updateWrapper.set(Article::getViewCount, article.getViewCount());
            service.update(updateWrapper);
        }
    }
}
