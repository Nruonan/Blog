package com.nruonan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nruonan.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Nruonan
 * @description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
