package com.yuhua.backgroundmanagement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuhua.backgroundmanagement.common.Constants;
import com.yuhua.backgroundmanagement.controller.dto.UserDTO;
import com.yuhua.backgroundmanagement.entity.Menu;
import com.yuhua.backgroundmanagement.entity.User;
import com.yuhua.backgroundmanagement.exception.ServiceException;
import com.yuhua.backgroundmanagement.mapper.RoleMapper;
import com.yuhua.backgroundmanagement.mapper.RoleMenuMapper;
import com.yuhua.backgroundmanagement.mapper.UserMapper;
import com.yuhua.backgroundmanagement.service.IMenuService;
import com.yuhua.backgroundmanagement.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhua.backgroundmanagement.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public UserDTO login(UserDTO userDTO) {
        User user =getUserInfo(userDTO);
        if(user!=null){
            BeanUtil.copyProperties(user,userDTO,true);
            String token=TokenUtils.getToken(user.getId().toString(),user.getPassword());
            userDTO.setToken(token);
            String role=user.getRole();
            //设置角色对应的菜单
            List<Menu> roleMenus=getRoleMenus(role);
            userDTO.setMenus(roleMenus);
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

    /**
     * 获取当前角色菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag){
        Integer roleId=roleMapper.selectByFlag(roleFlag);
        //当前角色的所有菜单id集合
        List<Integer> menuIds=roleMenuMapper.selectByRoleId(roleId);

        //获取菜单
        List<Menu> menus=menuService.findMenus("");
        //最后完成的结果
        List<Menu> roleMenus=new ArrayList<>();

        //筛选当前用户菜单
        for(Menu menu:menus){
            if(menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children=menu.getChildren();
            children.removeIf(child -> !menuIds.contains(child.getId()));

            /*List<Menu> children=menu.getChildren();
            children.removeIf(child -> !menuIds.contains(child.getId()));
            if(children.size()!=0){
                roleMenus.add(menu);
            }*/
        }
        return roleMenus;
    }
}
