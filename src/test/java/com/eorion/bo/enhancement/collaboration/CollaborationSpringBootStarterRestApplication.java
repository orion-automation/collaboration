package com.eorion.bo.enhancement.collaboration;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CollaborationSpringBootStarterRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollaborationSpringBootStarterRestApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(value = "camunda.bpm.security.enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<ProcessEngineAuthenticationFilter> basicAuthRestFilterRegistrationBean(ProcessEngine processEngine) {
        FilterRegistrationBean<ProcessEngineAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        ProcessEngineAuthenticationFilter filter = new ProcessEngineAuthenticationFilter();
        registrationBean.setName("camunda-auth");
        registrationBean.addInitParameter("authentication-provider",
                "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider");
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/engine-rest/*", "/enhancement/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

}
