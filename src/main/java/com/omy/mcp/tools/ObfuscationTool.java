package com.omy.mcp.tools;

import com.omy.mcp.config.HeaderContextHolder;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * 混淆计算工具类
 * 提供两个混淆计算工具：
 * 1. obfuscateCalculation: 两数相加后再加0.1
 * 2. obfuscateMultiply: 两数相乘后再乘以10
 */
@Service
public class ObfuscationTool {

    private static final Logger logger = LoggerFactory.getLogger(ObfuscationTool.class);

    @Tool(name = "obfuscateCalculation", description = "混淆加法工具：将两个数相加后再加0.1返回，同时会获取并返回请求头信息")
    public String obfuscateCalculation(
            @ToolParam(description = "第一个数字参数") double a,
            @ToolParam(description = "第二个数字参数") double b,
            ToolContext toolContext) {

        String headers = getHttpHeaders();
        String exchangeInfo = getExchangeInfo(toolContext);

        double result = a + b + 0.1;
        logger.info("混淆计算: {} + {} + 0.1 = {}", a, b, result);

        StringBuilder sb = new StringBuilder();
        sb.append("========== HTTP 请求头信息 ==========\n");
        sb.append(headers);
        sb.append("\n========== MCP Exchange 信息 ==========\n");
        sb.append(exchangeInfo);
        sb.append("\n========== 计算结果 ==========\n");
        sb.append(String.format("计算公式: %s + %s + 0.1 = %s", a, b, result));

        return sb.toString();
    }

    @Tool(name = "obfuscateMultiply", description = "混淆乘法工具：将两个数相乘后再乘以10返回，同时会获取并返回请求头信息")
    public String obfuscateMultiply(
            @ToolParam(description = "第一个数字参数") double a,
            @ToolParam(description = "第二个数字参数") double b,
            ToolContext toolContext) {

        String headers = getHttpHeaders();
        String exchangeInfo = getExchangeInfo(toolContext);

        double result = a * b * 10;
        logger.info("混淆乘法: {} * {} * 10 = {}", a, b, result);

        StringBuilder sb = new StringBuilder();
        sb.append("========== HTTP 请求头信息 ==========\n");
        sb.append(headers);
        sb.append("\n========== MCP Exchange 信息 ==========\n");
        sb.append(exchangeInfo);
        sb.append("\n========== 计算结果 ==========\n");
        sb.append(String.format("计算公式: %s * %s * 10 = %s", a, b, result));

        return sb.toString();
    }

    /**
     * 通过 HeaderContextHolder 获取 HTTP 请求头（由 RequestContextPropagationFilter 捕获）
     */
    private String getHttpHeaders() {
        Map<String, String> headers = HeaderContextHolder.get();
        if (headers.isEmpty()) {
            logger.warn("未获取到 HTTP 请求头，HeaderContextHolder 为空");
            return "未获取到 HTTP 请求头（可能工具未在 HTTP 请求线程或其子线程中执行）\n";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            logger.info("HTTP Header - {}: {}", entry.getKey(), entry.getValue());
        }
        return sb.toString();
    }

    /**
     * 通过 ToolContext + McpToolUtils 获取 MCP Exchange 信息
     */
    private String getExchangeInfo(ToolContext toolContext) {
        StringBuilder sb = new StringBuilder();
        try {
            if (toolContext == null) {
                return "ToolContext 为 null\n";
            }

            Optional<McpSyncServerExchange> exchangeOpt = McpToolUtils.getMcpExchange(toolContext);
            if (exchangeOpt.isPresent()) {
                McpSyncServerExchange exchange = exchangeOpt.get();
                var clientCapabilities = exchange.getClientCapabilities();
                if (clientCapabilities != null) {
                    sb.append("客户端能力: ").append(clientCapabilities).append("\n");
                }
                var clientInfo = exchange.getClientInfo();
                if (clientInfo != null) {
                    sb.append("客户端信息: ").append(clientInfo).append("\n");
                }
            } else {
                sb.append("McpSyncServerExchange 为空\n");
            }
        } catch (Exception e) {
            sb.append("获取 Exchange 信息异常: ").append(e.getMessage()).append("\n");
            logger.error("获取 Exchange 信息异常", e);
        }
        return sb.toString();
    }
}
