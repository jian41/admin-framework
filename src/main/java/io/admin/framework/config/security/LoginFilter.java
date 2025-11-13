package io.admin.framework.config.security;

import io.admin.common.dto.AjaxResult;
import io.admin.common.utils.ResponseUtils;
import io.admin.modules.common.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 自定义的登录逻辑
 */
@Slf4j
@AllArgsConstructor
public class LoginFilter implements Filter {

    private final AuthService authService;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();

        if (!uri.equals("/admin/auth/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String username = request.getParameter("username");
        try {
            authService.validate(request);
        } catch (Exception e) {
            log.error("用户[{}]认证失败： {}", username, e.getMessage());
            ResponseUtils.response(response, AjaxResult.err(e.getMessage()));
            return;
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
            authService.onSuccess(username);
        } catch (Exception e) {
            authService.onFail(username);
            ResponseUtils.response(response, AjaxResult.err(e.getMessage()));
        }
    }


}
