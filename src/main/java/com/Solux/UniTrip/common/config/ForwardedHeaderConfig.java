package com.Solux.UniTrip.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class ForwardedHeaderConfig {

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new ForwardedHeaderFilter());
        filterRegBean.setOrder(0); // ★ Security filter chain보다 앞에 오도록 설정
        return filterRegBean;
    }
}
