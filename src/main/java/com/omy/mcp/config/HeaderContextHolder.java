package com.omy.mcp.config;

import java.util.Collections;
import java.util.Map;

/**
 * 使用 InheritableThreadLocal 在请求线程和工具执行线程之间传递 HTTP 头信息。
 * MCP 工具可能在子线程中执行，普通 ThreadLocal 无法跨线程传播，
 * InheritableThreadLocal 可自动传播给子线程。
 */
public final class HeaderContextHolder {

    private static final InheritableThreadLocal<Map<String, String>> HEADERS =
            new InheritableThreadLocal<>();

    private HeaderContextHolder() {
    }

    public static void set(Map<String, String> headers) {
        HEADERS.set(headers);
    }

    public static Map<String, String> get() {
        Map<String, String> headers = HEADERS.get();
        return headers != null ? headers : Collections.emptyMap();
    }

    public static void clear() {
        HEADERS.remove();
    }
}
