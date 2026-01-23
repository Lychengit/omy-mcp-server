package com.omy.mcp.config;

import org.springframework.ai.mcp.server.autoconfigure.McpServerProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP服务器配置类
 * 将配置从application.properties移到Java代码中
 */
@Configuration
public class McpServerConfig {

    /**
     * 使用BeanPostProcessor修改Spring AI自动创建的McpServerProperties
     */
    @Bean
    public BeanPostProcessor mcpServerPropertiesCustomizer() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof McpServerProperties properties) {
                    properties.setEnabled(true);
                    properties.setName("omy-mcp-server");
                    properties.setVersion("0.0.1");
                    properties.setSseEndpoint("/sse");
                    properties.setSseMessageEndpoint("/mcp/message");
                }
                return bean;
            }
        };
    }
}
