package com.omy.mcp.tools;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

/**
 * 混淆计算工具类
 * 提供两个混淆计算工具：
 * 1. obfuscateCalculation: 两数相加后再加0.1
 * 2. obfuscateMultiply: 两数相乘后再乘以10
 */
@Service
public class ObfuscationTool {

    private static final Logger logger = LoggerFactory.getLogger(ObfuscationTool.class);

    /**
     * 混淆计算方法
     * 1. 获取并打印HTTP请求头信息
     * 2. 将两个入参相加后再加0.1返回
     *
     * @param a 第一个数字参数
     * @param b 第二个数字参数
     * @return 包含头信息和计算结果的字符串
     */
    @Tool(name = "obfuscateCalculation", description = "混淆加法工具：将两个数相加后再加0.1返回，同时会获取并返回请求头信息")
    public String obfuscateCalculation(
            @ToolParam(description = "第一个数字参数") double a,
            @ToolParam(description = "第二个数字参数") double b) {

        // 获取头信息
        String headers = getRequestHeadersString();
        logger.info("headers: {}", headers);

        // 执行混淆计算：两数相加后再加0.1
        double result = a + b + 0.1;

        logger.info("混淆计算: {} + {} + 0.1 = {}", a, b, result);

        // 拼接头信息和计算结果
        StringBuilder sb = new StringBuilder();
        sb.append("========== 请求头信息 ==========\n");
        sb.append(headers);
        sb.append("\n========== 计算结果 ==========\n");
        sb.append(String.format("计算公式: %s + %s + 0.1 = %s", a, b, result));

        return sb.toString();
    }

    /**
     * 混淆乘法计算方法
     * 1. 获取并打印HTTP请求头信息
     * 2. 将两个入参相乘后再乘以10返回
     *
     * @param a 第一个数字参数
     * @param b 第二个数字参数
     * @return 包含头信息和计算结果的字符串
     */
    @Tool(name = "obfuscateMultiply", description = "混淆乘法工具：将两个数相乘后再乘以10返回，同时会获取并返回请求头信息")
    public String obfuscateMultiply(
            @ToolParam(description = "第一个数字参数") double a,
            @ToolParam(description = "第二个数字参数") double b) {

        // 获取头信息
        String headers = getRequestHeadersString();

        // 执行混淆计算：两数相乘后再乘以10
        double result = a * b * 10;

        logger.info("混淆乘法: {} * {} * 10 = {}", a, b, result);

        // 拼接头信息和计算结果
        StringBuilder sb = new StringBuilder();
        sb.append("========== 请求头信息 ==========\n");
        sb.append(headers);
        sb.append("\n========== 计算结果 ==========\n");
        sb.append(String.format("计算公式: %s * %s * 10 = %s", a, b, result));

        return sb.toString();
    }

    /**
     * 获取HTTP请求头信息并返回字符串
     *
     * @return 请求头信息字符串
     */
    private String getRequestHeadersString() {
        StringBuilder sb = new StringBuilder();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                logger.info("========== 请求头信息开始 ==========");

                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    String headerValue = request.getHeader(headerName);
                    sb.append(headerName).append(": ").append(headerValue).append("\n");
                    logger.info("{}: {}", headerName, headerValue);
                }

                logger.info("========== 请求头信息结束 ==========");
            } else {
                sb.append("无法获取请求上下文，可能不在HTTP请求环境中");
                logger.warn("无法获取请求上下文，可能不在HTTP请求环境中");
            }
        } catch (Exception e) {
            sb.append("获取请求头信息时发生异常: ").append(e.getMessage());
            logger.error("获取请求头信息时发生异常: {}", e.getMessage());
        }
        return sb.toString();
    }
}
