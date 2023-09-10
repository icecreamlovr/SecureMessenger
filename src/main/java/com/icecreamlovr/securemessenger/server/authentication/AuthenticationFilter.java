package com.icecreamlovr.securemessenger.server.authentication;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Cookie[] cookies = req.getCookies();

        boolean authenticated = false;
        if (cookies != null){
            for (Cookie c : cookies) {
                if (!"user-token".equals(c.getName())) {
                    continue;
                }
                String email;
                try {
                    email = jwtUtil.verifyAndGetEmail(c.getValue());
                } catch (IllegalArgumentException ex) {
                    System.out.println("[INFO] JWT verification failed: " + ex.getMessage());
                    break;
                }
                req.setAttribute("email", email);
                authenticated = true;
                break;
            }
        }

        if (!authenticated) {
            System.out.println("[INFO] auth failed!");
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendRedirect("/login");
            return;
        }

        System.out.println("[INFO] auth sucessful!");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
