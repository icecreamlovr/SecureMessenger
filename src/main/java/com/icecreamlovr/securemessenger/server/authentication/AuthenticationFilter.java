package com.icecreamlovr.securemessenger.server.authentication;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

        System.out.println(">>> I am at auth filter lalala~");
        System.out.println(jwtUtil);
        System.out.println(">>> I am at auth filter lalala~ end");

        boolean authenticated = false;
        if (cookies != null){
            for (Cookie c : cookies) {
                if ("user-token".equals(c.getName())) {
                    System.out.println(">>>The user-token cookie is: " + c.getValue());
                    String email = jwtUtil.verifyAndGetEmail(c.getValue());
                    System.out.println(">>>The email is: " + email);
                    req.setAttribute("email", email);
                    authenticated = true;
                    break;
                }
            }
        }
        if (authenticated) {
            System.out.println(">>>auth sucessful!");
        } else {
            System.out.println(">>>auth failed!");
        }
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
