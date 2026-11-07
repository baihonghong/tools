package com.baihonghong.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用启动类
 *
 * @author baihonghong
 * @date 2026-11-07
 */
@SpringBootApplication
public class ToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolsApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("ID生成服务启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("========================================\n");
    }
}