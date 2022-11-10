package com.yuhua.backgroundmanagement.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.yuhua.backgroundmanagement.common.Result;
import com.yuhua.backgroundmanagement.entity.User;
import com.yuhua.backgroundmanagement.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @GetMapping("/example")
    public Result get(){
        Map<String,Object> map=new HashMap<>();
        map.put("x", CollUtil.newArrayList("周一","周二","周三","周四","周五","周六","周日"));
        map.put("y",CollUtil.newArrayList(100,200,300,400,500,600,700));
        return Result.success(map);
    }

    @GetMapping("/members")
    public Result getMembers(){
        List<User> list=userService.list();
        int q1=0;//第一季度
        int q2=0;//第二季度
        int q3=0;//第三季度
        int q4=0;//第四季度
        for (User user : list) {
            Date createTime = user.getCreatTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter){
                case Q1:
                    q1++;
                    break;
                case Q2:
                    q2++;
                    break;
                case Q3:
                    q3++;
                    break;
                case Q4:
                    q4++;
                    break;
                default:
                    break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }
}
