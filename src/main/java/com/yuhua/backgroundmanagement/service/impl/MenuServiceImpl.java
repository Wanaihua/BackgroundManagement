package com.yuhua.backgroundmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuhua.backgroundmanagement.entity.Menu;
import com.yuhua.backgroundmanagement.mapper.MenuMapper;
import com.yuhua.backgroundmanagement.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YuHua
 * @since 2022-11-09
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if(!name.isEmpty()){
            queryWrapper.like("name",name);
        }
        //查询所有数据
        List<Menu> list=list(queryWrapper);
        //找出所有的一级菜单
        List<Menu> parentList=list.stream().filter(menu -> menu.getPid()==null).collect(Collectors.toList());
        //找出所有的二级菜单
        for(Menu menu:parentList){
            //筛选所有数据中pid=父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentList;
    }
}
