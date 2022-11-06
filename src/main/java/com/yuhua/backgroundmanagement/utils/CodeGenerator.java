package com.yuhua.backgroundmanagement.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * mp代码生成器
 * by yuhua
 * @date 2020/12/15
 *
 **/
public class CodeGenerator {
    public static void main(String[] args) {
        generate();
    }
    private static void generate(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/BM?useSSL=false&serverTimezone=UTC", "root", "wanaihua")
                .globalConfig(builder -> {
                    builder.author("YuHua") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir() //禁止打开输出目录
                            .outputDir("D:\\IdeaProjects\\BackgroundManagement" + "/src/main/java/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.yuhua.backgroundmanagement") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\IdeaProjects\\BackgroundManagement" + "/src/main/resources/mapper")); // 设置mapperXml生成路径

                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok(); // 开启lombok
                    builder.controllerBuilder().enableRestStyle().enableHyphenStyle(); // 开启驼峰转换 开启restful api风格
    //                builder.mapperBuilder().enableBaseResultMap(); // 开启BaseResultMap
                    builder.addInclude("bm_user") // 设置需要生成的表名
                            .addTablePrefix("t_","bm_"); // 表前缀
                })
                .execute();


    }
}
