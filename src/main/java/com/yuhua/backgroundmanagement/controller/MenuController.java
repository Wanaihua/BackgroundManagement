package com.yuhua.backgroundmanagement.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.yuhua.backgroundmanagement.common.Result;
import java.util.List;
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

    @Autowired
    private IMenuService menuService;

    //新增或更新
    @PostMapping
    public Result save(@RequestBody Menu menu){
        return Result.success(menuService.saveOrUpdate(menu));
    }

    //查询所有
    @GetMapping
    public List<Menu> findAll() {
        return menuService.list();
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

}

