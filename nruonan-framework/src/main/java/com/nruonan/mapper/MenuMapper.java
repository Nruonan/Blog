package com.nruonan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nruonan.domain.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Long> selectMenuListByRoleId(Long id);
}
