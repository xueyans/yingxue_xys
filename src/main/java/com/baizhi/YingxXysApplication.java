package com.baizhi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



//给当前包下的dao起别名
@tk.mybatis.spring.annotation.MapperScan("com.baizhi.dao")
@org.mybatis.spring.annotation.MapperScan("com.baizhi.dao")
@SpringBootApplication
public class YingxXysApplication {

    public static void main(String[] args) {
        SpringApplication.run(YingxXysApplication.class, args);
    }

}
