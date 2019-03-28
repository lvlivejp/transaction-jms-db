package com.example.transactionjms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = "com.example.transactionjms.dao")
public class MybatisConfig {

}
