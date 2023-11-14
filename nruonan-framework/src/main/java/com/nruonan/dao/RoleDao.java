package com.nruonan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nruonan.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-10 15:21:55
 */
@Mapper
public interface RoleDao extends BaseMapper<Role> {

}

