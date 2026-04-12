package com.sc.qisi_system.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;

@Component
public class RequestUtils {

    /**
     * 统一获取请求信息：IP + 请求路径 + 请求方法
     */
    public String getRequestLog(HttpServletRequest request) {
        return "IP= " + getClientIp(request) + ", URI= " + request.getRequestURI() + ", METHOD= " + request.getMethod();
    }

    /**
     * 获取客户端真实IP
     * 兼容：Nginx代理、多级代理、IPv4/IPv6、本地回环
     */
    public String getClientIp(HttpServletRequest request) {
        String ip;

        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED"
        };

        for (String header : headers) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        ip = request.getRemoteAddr();

        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }




    public String getWebSocketRequestLog(ServerHttpRequest request) {
        String ip = getClientIp(request);
        String uri = request.getURI().toString();
        return "IP= " + ip + ", URI= " + uri + ", METHOD= WEBSOCKET";
    }

    public String getClientIp(ServerHttpRequest request) {
        String ip;
        HttpHeaders headers = request.getHeaders();

        String[] headerKeys = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };

        for (String header : headerKeys) {
            ip = headers.getFirst(header);
            if (isValidIp(ip)) {
                if (ip != null) {
                    return ip.split(",")[0].trim();
                }
            }
        }

        InetSocketAddress remoteAddr = request.getRemoteAddress();
        ip = remoteAddr.getAddress().getHostAddress();

        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }

    /**
     * 校验IP是否有效（非空、非unknown）
     */
    private static boolean isValidIp(String ip) {
        return StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip);
    }


}
