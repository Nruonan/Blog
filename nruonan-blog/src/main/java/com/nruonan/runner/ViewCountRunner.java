package com.nruonan.runner;

import com.nruonan.constants.SystemConstants;
import com.nruonan.domain.entity.Article;
import com.nruonan.mapper.ArticleMapper;
import com.nruonan.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Nruonan
 * @description
 */
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Resource
    RedisCache redisCache;

    @Resource
    private ArticleMapper mapper;
    @Override
    //下面的写法是stream流+方法引用+Lambda。我们用这种写法使得代码更简短，跟上面那种写法的效果是一样的
    public void run(String... args) throws Exception {
        //查询数据库中的博客信息，注意只需要查询id、viewCount字段的博客信息
        List<Article> articles = mapper.selectList(null);//为null即无条件，表示查询所有
        Map<String, Integer> viewCountMap = articles.stream()
                //由于我们需要key、value的数据，所有可以通过stream流

                //下面toMap方法的第一个参数用了方法引用，第二个参数用了Lambda
                //.collect(Collectors.toMap(Article::getId, article -> article.getViewCount().intValue()));

                //由于上面那行Article::getId返回值是Long类型，而我们需要String类型，为了方便转换类型，我们要写成Lambda表达式
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));


        //把查询到的id作为key，viewCount作为value，一起存入Redis
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}
