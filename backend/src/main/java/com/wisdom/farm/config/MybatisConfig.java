package com.wisdom.farm.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.wisdom.farm.mapper")
public class MybatisConfig {
}
