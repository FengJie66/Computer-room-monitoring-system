package com.alibb.system;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.alibb.system.mapper")
@SpringBootApplication
@EnableSpringUtil
public class MonitorSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorSystemApplication.class, args);
	}

}
