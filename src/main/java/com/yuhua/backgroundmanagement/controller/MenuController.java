package com.yuhua.backgroundmanagement.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuhua.backgroundmanagement.common.Constants;
import com.yuhua.backgroundmanagement.entity.Dict;
import com.yuhua.backgroundmanagement.mapper.DictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.yuhua.backgroundmanagement.common.Result;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.yuhua.backgroundmanagement.service.IMenuService;
import com.yuhua.backgroundmanagement.entity.Menu;




import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YuHua
 * @since 2022-11-09
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private DictMapper dictMapper;

    //新增或更新
    @PostMapping
    public Result save(@RequestBody Menu menu){
        return Result.success(menuService.saveOrUpdate(menu));
    }

    //查询所有
    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        //查询所有数据
        List<Menu> list=menuService.list(queryWrapper);
        //找出所有的一级菜单
        List<Menu> parentNode=list.stream().filter(menu -> menu.getPid()==null).collect(Collectors.toList());
        //找出所有的二级菜单
        for(Menu menu:parentNode){
            //筛选所有数据中pid=父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return Result.success(parentNode);
    }

    //根据id删除
    @DeleteMapping("/{id}")
    public Result Delete(@PathVariable Integer id){
        return Result.success(menuService.removeById(id));
    }

    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(menuService.removeByIds(ids));
    }

    //分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize,@RequestParam(defaultValue = "") String name){
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByDesc("id");
        return Result.success(menuService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

    @GetMapping("/icons")
    public Result getIcons(){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(null));
    }
}

