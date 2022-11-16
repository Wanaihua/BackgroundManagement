package com.yuhua.backgroundmanagement.controller.dto;

import com.yuhua.backgroundmanagement.entity.Menu;
import lombok.Data;

import java.util.List;

/*
接受前端登录传来的参数
 */
@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private List<Menu> menus;
    private String role;

}
