package com.yuhua.backgroundmanagement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuhua.backgroundmanagement.common.Constants;
import com.yuhua.backgroundmanagement.controller.dto.UserDTO;
import com.yuhua.backgroundmanagement.entity.User;
import com.yuhua.backgroundmanagement.exception.ServiceException;
import com.yuhua.backgroundmanagement.mapper.UserMapper;
import com.yuhua.backgroundmanagement.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhua.backgroundmanagement.utils.TokenUtils;
import org.springframework.stereotype.Service;

import java.util.Queue;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YuHua
 * @since 2022-10-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static  final  Log LOG= Log.get();

    @Override
    public UserDTO login(UserDTO userDTO) {
        User user =getUserInfo(userDTO);
        if(user!=null){
            BeanUtil.copyProperties(user,userDTO,true);
            String token=TokenUtils.getToken(user.getId().toString(),user.getPassword());
            userDTO.setToken(token);
            return userDTO;
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        User user=getUserInfo(userDTO);
        if(user!=null){
            throw new ServiceException(Constants.CODE_600,"用户名已存在");
        }else {
            user=new User();
            BeanUtil.copyProperties(userDTO,user,true);
            save(user);
        }
        return user;
    }

    private User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User user;
        try{
            user=getOne(queryWrapper);
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"服务器异常");
        }
        return user;
    }
}
