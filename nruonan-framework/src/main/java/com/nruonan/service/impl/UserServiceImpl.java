package com.nruonan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nruonan.ResponseResult;
import com.nruonan.domain.entity.User;
import com.nruonan.domain.entity.UserRole;
import com.nruonan.domain.vo.PageVo;
import com.nruonan.domain.vo.UserInfoVo;
import com.nruonan.domain.vo.UserListVo;
import com.nruonan.enums.AppHttpCodeEnum;
import com.nruonan.handler.exception.SystemException;
import com.nruonan.mapper.UserMapper;
import com.nruonan.service.UserRoleService;
import com.nruonan.service.UserService;
import com.nruonan.utils.BeanCopyUtils;
import com.nruonan.utils.SecurityUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-11-08 10:03:40
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Resource
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult userInfo() {
        // 通过Authentication获取id
        Long userId = SecurityUtils.getUserId();
        // 根据id 通过mybatisplus获取对象
        User user = getById(userId);
        // 转换成对象
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) throws SystemException {
        // 对user数据判断非空判断
        if(user.getUserName() == null){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }else if(user.getPassword() == null){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }else if(user.getEmail() == null){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }else if(user.getNickName() == null){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }

        // 对数据进行是否存在的判断
        if(userNameExit(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(emailExit(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 对密码进行加密
        String encode = passwordEncoder.encode(user.getPassword());
        // 存入数据库
        user.setPassword(encode);
        save(user);
        return ResponseResult.okResult();
    }

    private boolean emailExit(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail,email);
        int count = count(wrapper);
        if(count > 0){
            return true;
        }
        return false;
    }

    private boolean userNameExit(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        int count = count(wrapper);
        if(count > 0){
            return true;
        }
        return false;
    }

    @Override
    public ResponseResult Userslist(User user, Integer pageNum, Integer pageSize) {
         LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
         wrapper.eq(StringUtils.hasText(user.getPhonenumber()),User::getPhonenumber,user.getPhonenumber())
                 .like(StringUtils.hasText(user.getUserName()),User::getUserName,user.getUserName())
                 .eq(StringUtils.hasText(user.getStatus()),User::getStatus,user.getStatus());
        Page<User> page = new Page(pageNum,pageSize);
        page(page,wrapper);
        List<User> userList = page.getRecords();
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(userList, UserListVo.class);

        return ResponseResult.okResult(new PageVo(userListVos,page.getTotal()));
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(userName),User::getUserName,userName);

        int count = count(wrapper);
        if(count > 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(user.getPhonenumber()),User::getPhonenumber,user.getPhonenumber());

        int count = count(wrapper);
        if(count > 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(user.getEmail()),User::getEmail,user.getEmail());
        int count = count(wrapper);
        if(count > 0){
            return false;
        }
        return true;
    }

    @Override
    public ResponseResult addUser(User user) {
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }
    @Resource
    private UserRoleService userRoleService;
    private void insertUserRole(User user) {
        Long[] roleIds = user.getRoleIds();
        List<UserRole> collect = Arrays.stream(roleIds).map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(collect);
    }
    // 多重删除
    @Override
    public ResponseResult delete(List<Long> ids) {
        LambdaQueryWrapper<User> wrapper =new LambdaQueryWrapper<>();
        wrapper.in(User::getId,ids);
        remove(wrapper);
        return ResponseResult.okResult();
    }

    //-----------------------------修改用户-②更新用户信息-------------------------------

    @Override
    @Transactional
    public ResponseResult updateUser(User user) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(wrapper);
        // 新增用户与角色管理
        insertUserRole(user);
        // 更新用户信息
        updateById(user);
        return ResponseResult.okResult();
    }
}
