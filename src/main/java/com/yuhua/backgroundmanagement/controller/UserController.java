package com.yuhua.backgroundmanagement.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuhua.backgroundmanagement.common.Constants;
import com.yuhua.backgroundmanagement.common.Result;
import com.yuhua.backgroundmanagement.controller.dto.UserDTO;
import com.yuhua.backgroundmanagement.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.MulticastSocket;
import java.net.URLEncoder;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.yuhua.backgroundmanagement.service.IUserService;
import com.yuhua.backgroundmanagement.entity.User;




import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YuHua
 * @since 2022-10-21
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private IUserService userService;

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO userDto=userService.login(userDTO);
        return Result.success(userDto);
    }

    //注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        User user=userService.register(userDTO);
        return Result.success(user);
    }


    //新增或更新
    @PostMapping
    public Result save(@RequestBody User user){
        return Result.success(userService.saveOrUpdate(user));
    }

    //查询所有
    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper));
    }

    //根据id删除
    @DeleteMapping("/{id}")
    public Result Delete(@PathVariable Integer id){
        return Result.success(userService.removeById(id));
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(userService.removeByIds(ids));
    }

    //分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,@RequestParam(defaultValue = "") String username,@RequestParam(defaultValue = "") String email,@RequestParam(defaultValue = "") String address) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if(!"".equals(username)) {
            queryWrapper.like("username",username);
        }
        if(!"".equals(email)) {
            queryWrapper.like("email",email);
        }
        if(!"".equals(address)) {
            queryWrapper.like("address",address);
        }
        //获取当前用户信息
        User currentUser=TokenUtils.getCurrentUser();
        System.out.println(currentUser.getUsername());

        return Result.success(userService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

    //导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<User> list = userService.list();

        //导出操作
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //导出路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath+"/用户信息.xlsx");

        //通过构造方法创建writer，默认创建xls格式
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("creatTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");

        //一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);


        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlfromats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        // 关闭writer，释放内存
        out.close();
        writer.close();
    }

    //导入
    @PostMapping("/import")
    public boolean importExcel(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
 /*       List<User> list = reader.readAll(User.class);
        userService.saveBatch(list);*/

        //忽略表头中文
        List<List<Object>> list=reader.read(1);
        List<User> users= CollUtil.newArrayList();
        for(List<Object> rom:list){
            User user=new User();
            user.setUsername(rom.get(1).toString());
            user.setPassword(rom.get(2).toString());
            user.setNickname(rom.get(3).toString());
            user.setEmail(rom.get(4).toString());
            user.setPhone(rom.get(5).toString());
            user.setAddress(rom.get(6).toString());
            user.setAvatarUrl(rom.get(7).toString());
            users.add(user);
        }
        System.out.println(users);
        userService.saveBatch(users);
        return true;
    }
}

