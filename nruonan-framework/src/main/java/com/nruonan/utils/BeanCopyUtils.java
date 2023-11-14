package com.nruonan.utils;

import com.nruonan.domain.entity.Article;
import com.nruonan.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nruonan
 * @description
 */
public class BeanCopyUtils {
    private BeanCopyUtils() {

    }
    public static <V> V copyBean(Object source , Class<V> clazz){
        // 创建目标对象
        V res = null;
        try {
            res = clazz.newInstance();
            // 实现属性copy
            BeanUtils.copyProperties(source, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    //2.集合的拷贝(在ArticleServiceImpl类里面会使用到)。第一个参数是要拷贝的集合，第二个参数是类的字节码对象
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o,clazz))
                .collect(Collectors.toList());
    }
}
