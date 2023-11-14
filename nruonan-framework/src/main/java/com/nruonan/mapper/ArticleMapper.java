package com.nruonan.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nruonan.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Nruonan
 * @description
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
