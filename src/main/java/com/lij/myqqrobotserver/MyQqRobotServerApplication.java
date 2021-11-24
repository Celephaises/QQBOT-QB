package com.lij.myqqrobotserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Celphis
 */
@SpringBootApplication
@MapperScan("com.lij.myqqrobotserver")
public class MyQqRobotServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyQqRobotServerApplication.class, args);
	}

}
