package com.nruonan.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Nruonan
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserListVo {
    //主键
    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;


    //账号状态（0正常 1停用）
    private String status;

    //手机号
    private String phonenumber;

    private String email;
    //创建时间
    private Date createTime;

    private String sex;



    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
