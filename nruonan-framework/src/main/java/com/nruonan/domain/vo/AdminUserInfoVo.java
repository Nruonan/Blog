package com.nruonan.domain.vo;

import com.qiniu.rtc.model.RoomResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Nruonan
 * @description
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class AdminUserInfoVo {
    private List<String> permissions;
    private List<String> roles;
    private UserInfoVo user;
}
