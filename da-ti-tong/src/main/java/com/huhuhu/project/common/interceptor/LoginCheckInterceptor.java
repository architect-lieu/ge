package com.huhuhu.project.common.interceptor;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @author kangxin
 * @Date 2022/4/6 15:27
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    public static final Pattern ADMIN_PATTERN = Pattern.compile("^/admin");
    public static final Pattern CUSTOMER_PATTERN = Pattern.compile("^/customer");

    private static final Log ACCESS_LOG = LogFactory.getLog("access_log");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        response.addHeader("Access-Control-Max-Age", "2592000");
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }
        boolean check = TokenUtil.check();
        if (!check) {
            handlerResult(response);
            return false;
        }
        // 获取登录用户的类型
        String path = request.getServletPath();
        String type = TokenUtil.currentLoginUserType();
        if ((type.equals(SystemConstant.CUSTOMER) && !CUSTOMER_PATTERN.matcher(path).find())
                || (type.equals(SystemConstant.ADMIN) && !ADMIN_PATTERN.matcher(path).find())) {
            throw new BusinessException(ResultCode.TOKEN_GET_ERROR);
        }
        return true;
    }

    private static void handlerResult(HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        String result = JSON.toJSONString(CommonResult.failed(ResultCode.TOKEN_GET_ERROR.getMessage()));
        try (PrintWriter writer = response.getWriter()){
            writer.write(result);
            writer.flush();
        }catch (Exception e) {
            //
            e.printStackTrace();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        //domain & uri & refer
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return;
        }
        String domain = request.getServerName();
        String uri = request.getRequestURI();
        Integer statusCode = response.getStatus();
        String remoteIp = getRemoteAddr(request);
        Long userId = TokenUtil.currentLoginUserId();
        printAccessLog(remoteIp, domain, uri, statusCode, userId);
    }


    private void printAccessLog(String remoteIp, String domain, String uri
            , Integer statusCode, Long userId) {

        StringBuilder sb = new StringBuilder();

        String date = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        sb.append(date);
        sb.append(" -[").append(remoteIp);
        //RequestId用于定位access log与业务log
        sb.append("]- ").append(domain);
        sb.append(" -[").append(uri);
        sb.append("]- ").append(statusCode);
        sb.append(" - user_id:").append(userId);
        //将参数map打印成json格式，利于统计分析

        ACCESS_LOG.info(sb.toString());
    }

    private String getRemoteAddr(HttpServletRequest request) {
        // 打印所有日志
        if (log.isDebugEnabled()) {
            log.info("X-Forwarded-For:" + request.getHeader
                    ("X-Forwarded-For") +
                    "\tProxy-Client-IP:" + request.getHeader("Proxy-Client-IP") +
                    "\t:WL-Proxy-Client-IP:" +
                    request.getHeader("WL-Proxy-Client-IP") + "\tRemoteAddr:" + request
                    .getRemoteAddr());
        }

        String ip;
        Enumeration<String> xffs = request.getHeaders("X-Forwarded-For");
        if (xffs.hasMoreElements()) {
            String xff = xffs.nextElement();
            ip = resolveClientIpFromXff(xff);
            if (isValidIp(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 从X-Forwarded-For头部中获取客户端的真实IP。 X-Forwarded-For并不是RFC定义的标准HTTP请求Header
     * ，可以参考http://en.wikipedia.org/wiki/X-Forwarded-For
     *
     * @param xff X-Forwarded-For头部的值
     * @return 如果能够解析到client IP，则返回表示该IP的字符串，否则返回null
     */
    private String resolveClientIpFromXff(String xff) {
        if (xff == null || xff.isEmpty()) {
            return null;
        }
        String[] ss = xff.split(",");
        // x-forward-for链反向遍历
        for (int i = ss.length - 1; i >= 0; i--) {
            String ip = ss[i].trim();
            if (isValidIp(ip)) {
                return ip;
            }
        }

        return null;
    }

    private static final Pattern IP_PATTERN = Pattern.compile("([0-9]{1,3}\\.){3}[0-9]{1,3}");

    private boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        return IP_PATTERN.matcher(ip).matches();
    }
}
