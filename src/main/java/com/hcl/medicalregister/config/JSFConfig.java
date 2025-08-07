package com.hcl.medicalregister.config;

import jakarta.faces.webapp.FacesServlet;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSFConfig {

    @Bean
    public ServletRegistrationBean<FacesServlet> facesServlet() {
        ServletRegistrationBean<FacesServlet> servlet = new ServletRegistrationBean<>(
                new FacesServlet(), "*.xhtml");
        servlet.setLoadOnStartup(1);
        return servlet;
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            servletContext.setInitParameter("jakarta.faces.FACELETS_SKIP_COMMENTS", "true");
        };
    }
}

