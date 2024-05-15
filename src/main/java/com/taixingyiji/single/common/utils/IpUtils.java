package com.taixingyiji.single.common.utils;



import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className IpUtils
 * @date 2022年06月23日 15:16
 * @description 描述
 */
@Slf4j
public class IpUtils {

    /**
     * 获取 链接请求源地址
     * @param request  请求头
     * @return 源地址
     */
    public static String getReferer(HttpServletRequest request) throws Exception {
        if (request == null) {
            throw new Exception("getReferer method HttpServletRequest Object is null  请求对象是null");
        }
        // 获取请求是从哪里来的
        String referer = request.getHeader("referer");
        if(StringUtils.isNotBlank(referer)){
            return referer;
        }else {
            return null;
        }
    }


    /**
     * 获取真实ID地址，首先从MDC 获取，如果MDC获取失败，再从HttpServletRequest中获取
     * @param request
     * @return
     * @throws Exception
     */
    public static String getIp(HttpServletRequest request) throws Exception {

        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        Gson gson = new Gson();
        log.debug("getIp contextMap = {}", gson.toJson(contextMap));

        //首先从MDC中获取IP ,如果获取不到再从Header中获取
        if(contextMap == null
                || StringUtils.isBlank(contextMap.get("clientIp"))){
            if (request == null) {
                throw new Exception("getIpAddr method HttpServletRequest Object is null  请求对象是null");
            }
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()){
                String next = headerNames.nextElement();
                log.info("getIpAddr headerNames , next = {}, value = {}",next, request.getHeader(next));
            }
            // 获取ip地址
            String ipAddress = "";
            if (StringUtils.isNotBlank(checkIp(request.getHeader("X-Real-IP")))) {
                ipAddress = request.getHeader("X-Real-IP");
            }else if (StringUtils.isNotBlank(checkIp(request.getHeader("x-real-ip")))) {
                ipAddress = request.getHeader("x-real-ip");
            } else if (StringUtils.isNotBlank(checkIp(request.getHeader("x-forwarded-for")))) {
                ipAddress = request.getHeader("x-forwarded-for");
            }else if (StringUtils.isNotBlank(checkIp(request.getHeader("Proxy-Client-IP")))) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }else if (StringUtils.isNotBlank(checkIp(request.getHeader("proxy-client-ip")))) {
                ipAddress = request.getHeader("proxy-client-ip");
            } else if (StringUtils.isNotBlank(checkIp(request.getHeader("HTTP_CLIENT_IP")))) {
                ipAddress = request.getHeader("HTTP_CLIENT_IP");
            } else if (StringUtils.isNotBlank(checkIp(request.getHeader("http_client_ip")))) {
                ipAddress = request.getHeader("http_client_ip");
            }else if (StringUtils.isNotBlank(checkIp(request.getHeader("WL-Proxy-Client-IP")))) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }else if (StringUtils.isNotBlank(checkIp(request.getHeader("wl-proxy-client-ip")))) {
                ipAddress = request.getHeader("wl-proxy-client-ip");
            }
            if(StringUtils.isBlank(ipAddress)){
                ipAddress = request.getRemoteAddr();
            }
            log.debug("getIp befor ipAddress = {}", gson.toJson(ipAddress));
            // 多个路由时，取第一个非unknown的ip
            final String[] arr = ipAddress.split(",");
            for (final String str : arr) {
                if (!"unknown".equalsIgnoreCase(str)) {
                    ipAddress = str;
                    break;
                }
            }
            log.debug("getIp after ipAddress = {}", gson.toJson(ipAddress));
            return ipAddress;
        }else{
            return contextMap.get("clientIp");
        }
    }

    /**
     * 检测IP内容
     * @param ip ip地址
     * @return 检测后的IP内容，如果为空则代表IP格式错误
     */
    private static String checkIp(String ip) {
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return null;
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    public static String getRealIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}


