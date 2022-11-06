package com.yuhua.backgroundmanagement.service;

import com.yuhua.backgroundmanagement.controller.dto.UserDTO;
import com.yuhua.backgroundmanagement.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YuHua
 * @since 2022-10-21
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);
}
