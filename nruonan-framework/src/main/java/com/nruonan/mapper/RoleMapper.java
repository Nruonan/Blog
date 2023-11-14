package com.nruonan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nruonan.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(Long userId);

    List<Long> selectRoleIdByUserId(Long id);
}
