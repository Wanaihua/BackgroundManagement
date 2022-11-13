package com.yuhua.backgroundmanagement.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.yuhua.backgroundmanagement.common.Result;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.yuhua.backgroundmanagement.service.IDictService;
import com.yuhua.backgroundmanagement.entity.Dict;




import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YuHua
 * @since 2022-11-11
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private IDictService dictService;

    //新增或更新
    @PostMapping
    public Result save(@RequestBody Dict dict){
        return Result.success(dictService.saveOrUpdate(dict));
    }

    //查询所有
    @GetMapping
    public List<Dict> findAll() {
        return dictService.list();
    }

    //根据id删除
    @DeleteMapping("/{id}")
    public Result Delete(@PathVariable Integer id){
        return Result.success(dictService.removeById(id));
    }

    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(dictService.removeByIds(ids));
    }

    //分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(dictService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

}

