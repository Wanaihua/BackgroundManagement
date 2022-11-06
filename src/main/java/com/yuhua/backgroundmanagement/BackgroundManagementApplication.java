package com.yuhua.backgroundmanagement;

import com.yuhua.backgroundmanagement.entity.User;
import com.yuhua.backgroundmanagement.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class BackgroundManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackgroundManagementApplication.class, args);
    }



}
