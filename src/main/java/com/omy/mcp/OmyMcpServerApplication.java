package com.omy.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MCP服务器主启动类
 * 基于Spring AI的MCP服务，提供混淆计算工具
 */
@SpringBootApplication
public class OmyMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmyMcpServerApplication.class, args);
    }
}
