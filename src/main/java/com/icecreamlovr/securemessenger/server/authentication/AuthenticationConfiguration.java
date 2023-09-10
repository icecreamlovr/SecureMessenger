package com.icecreamlovr.securemessenger.server.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfiguration {

    @Autowired
    private AuthenticationFilter authFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean() {
        FilterRegistrationBean <AuthenticationFilter> registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/messenger");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
