package com.nruonan.domain.vo;


import com.nruonan.domain.entity.Role;
import com.nruonan.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 35238
 * @date 2023/8/10 0010 21:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAndRoleIdsVo {
    private User user;
    private List<Role> roles;
    private List<Long> roleIds;
}