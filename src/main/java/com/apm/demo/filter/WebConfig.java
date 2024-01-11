package com.apm.demo.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean userFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("user security filter");
        UserFilter userFilter = new UserFilter();
        registrationBean.setFilter(userFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean requestFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("request security filter");
        RequestFilter requestFilter = new RequestFilter();
        registrationBean.setFilter(requestFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean payloadFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("payload security filter");
        PayloadFilter payloadFilter = new PayloadFilter();
        registrationBean.setFilter(payloadFilter);
        registrationBean.setOrder(3);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean ipFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("payload security filter");
        IpFilter ipFilter = new IpFilter();
        registrationBean.setFilter(ipFilter);
        registrationBean.setOrder(4);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean accessLogFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("payload security filter");
        AccessLogFilter accessLogFilter = new AccessLogFilter();
        registrationBean.setFilter(accessLogFilter);
        registrationBean.setOrder(5);
        return registrationBean;
    }
}
