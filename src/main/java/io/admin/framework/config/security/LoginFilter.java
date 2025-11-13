package io.admin.framework.config.security;

import io.admin.common.dto.AjaxResult;
import io.admin.common.utils.ResponseUtils;
import io.admin.modules.common.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * 自定义的登录逻辑
 */
@Slf4j
@AllArgsConstructor
public class LoginFilter extends OncePerRequestFilter {

    private final AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        boolean isLoginUrl = uri.equals("/admin/auth/login") && method.equals("POST");
        if (!isLoginUrl) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            password = authService.validate(request);
            request = new PasswordDecodingRequestWrapper(request, password);
        } catch (Exception e) {
            log.error("用户[{}]认证失败： {}", username, e.getMessage());
            ResponseUtils.response(response, AjaxResult.err(e.getMessage()));
            return;
        }

        try {
            filterChain.doFilter(request, response);
            authService.onSuccess(username);
        } catch (Exception e) {
            authService.onFail(username);
            ResponseUtils.response(response, AjaxResult.err(e.getMessage()));
        }
    }

    static class PasswordDecodingRequestWrapper extends HttpServletRequestWrapper {

        private String rawPassword;

        public PasswordDecodingRequestWrapper(HttpServletRequest request, String rawPassword) {
            super(request);
            this.rawPassword = rawPassword;
        }

        @Override
        public String getParameter(String name) {
            if (name.equals("password")) {
                return rawPassword;
            }
            return super.getParameter(name);
        }


    }

}
