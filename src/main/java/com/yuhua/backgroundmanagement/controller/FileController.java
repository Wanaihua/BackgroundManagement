package com.yuhua.backgroundmanagement.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuhua.backgroundmanagement.common.Result;
import com.yuhua.backgroundmanagement.entity.Files;
import com.yuhua.backgroundmanagement.entity.User;
import com.yuhua.backgroundmanagement.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/*
文件上传
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String uploadPath;

    @Resource
    private FileMapper fileMapper;


    //分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if(!"".equals(name)) {
            queryWrapper.like("name",name);
        }
        return Result.success(fileMapper.selectPage(new Page<Files>(pageNum,pageSize),queryWrapper));
    }

    //根据id删除
    @DeleteMapping("/{id}")
    public Result Delete(@PathVariable Integer id){
        Files files= fileMapper.selectById(id);
        files.setDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    //批量删除
    @PostMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files) {
            file.setDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    //更新
    @PostMapping("/update")
    public Result save(@RequestBody Files files){
        return Result.success(fileMapper.updateById(files));
    }

    /**
     * 文件上传接口
     * @param file 前端传过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String type= FileUtil.extName(fileName);
        long size = file.getSize();

        //定义一个文件唯一标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID=uuid+ StrUtil.DOT+type;
        File uploadFile=new File(uploadPath+fileUUID);
        //判断文件夹是否存在,不存在则创建
        if(!uploadFile.getParentFile().exists()){
            uploadFile.getParentFile().mkdirs();
        }

        String url="";
        String md5="";
        //上传文件到磁盘
        file.transferTo(uploadFile);
        //获取文件的md5值
        md5= SecureUtil.md5(uploadFile);
        //查询数据库判断文件是否存在
        Files files=getFileMd5(md5);
        if(files!=null){
            url=files.getUrl();
            //由于已存在文件,删除刚才上传的文件
            uploadFile.delete();
        }else{
            //数据库不存在则不删除
            url = "http://localhost:8090/file/"+fileUUID;
        }


        //存储数据
        Files saveFile=new Files();
        saveFile.setName(fileName);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        saveFile.setEnable(true);
        fileMapper.insert(saveFile);
        return url;
    }

    /**
     * 文件下载接口
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        //根据唯一标识符获取文件名
        File downloadFile=new File(uploadPath+fileUUID);
        ServletOutputStream os=response.getOutputStream();
        //设置输出流格式
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileUUID,"UTF-8"));
        response.setContentType("application/octet-stream");
        //读取文件的字节流
        os.write(FileUtil.readBytes(downloadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5判断文件是否存在
     * @param md5
     * @return
     */
    private Files getFileMd5(String md5) {
        QueryWrapper<Files> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> filesList=fileMapper.selectList(queryWrapper);
        return filesList.size()==0?null:filesList.get(0);
    }
}
