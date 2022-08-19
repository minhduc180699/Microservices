package com.saltlux.deepsignal.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class HttpRequestResponseUtils {

    private HttpRequestResponseUtils() {}

    private static final String[] IP_HEADER_CANDIDATES = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR",
    };

    public static String getClientIpAddress() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                String ip = ipList.split(",")[0];
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    public static String getRequestUrl() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getRequestURL().toString();
    }

    public static String getRequestUri() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getRequestURI();
    }

    public static String getRefererPage() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String referer = request.getHeader("Referer");

        return referer != null ? referer : request.getHeader("referer");
    }

    public static String getPageQueryString() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getQueryString();
    }

    public static String getUserAgent() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String userAgent = request.getHeader("User-Agent");

        return userAgent != null ? userAgent : request.getHeader("user-agent");
    }

    public static String getRequestMethod() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getMethod();
    }

    public static String getLoggedInUser() {
        String userJson = null;
        User user = getCurrentUser();

        if (user != null) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                userJson = mapper.writeValueAsString(user);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return userJson;
        }

        return userJson;
    }

    public static User getCurrentUser() {
        User user = null;
        if (
            SecurityContextHolder.getContext().getAuthentication() != null &&
            SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
            !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
        ) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return user;
    }

    public static String getHeader(String headerKey) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String headerValue = "";
        try {
            byte[] decodedBytes = Base64.decodeBase64(request.getHeader(headerKey));
            headerValue = new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.getMessage();
        }
        return headerValue;
    }

    public static String getUrlProtocol() {
        try {
            HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            URL url = new URL(request.getRequestURL().toString());
            return url.getProtocol() + "://" + request.getServerName() + ":" + request.getServerPort();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getBaseRefererPage() {
        String[] urlSplit = getRefererPage().split("/");
        StringBuilder baseUrl = new StringBuilder();
        if (urlSplit.length <= 3) {
            return getRefererPage();
        }
        for (int i = 3; i < urlSplit.length; i++) {
            baseUrl.append("/").append(urlSplit[i]);
        }
        return baseUrl.toString();
    }
}
