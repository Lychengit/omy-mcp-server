package com.omy.mcp.config;

import com.omy.mcp.tools.ObfuscationTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP工具配置类
 * 将工具注册到MCP服务器
 */
@Configuration
public class McpToolConfig {

    /**
     * 注册混淆计算工具到MCP服务器
     */
    @Bean
    public ToolCallbackProvider obfuscationToolCallbackProvider(ObfuscationTool obfuscationTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(obfuscationTool)
                .build();
    }
}
