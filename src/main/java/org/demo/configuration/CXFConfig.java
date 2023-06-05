package org.demo.configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import io.swagger.annotations.Api;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.validation.BeanValidationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ext.ExceptionMapper;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

@Configuration
public class CXFConfig {

    private Bus bus;

    private ApplicationContext applicationContext;

    private JacksonJsonProvider provider;

    private BeanValidationFeature beanValidationFeature;

    @Bean
    public Server rsServer() {
        final JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setAddress("/shelter");
        endpoint.setProvider(provider);
        List<Object> apiBeans = new ArrayList<>(applicationContext.getBeansWithAnnotation(Api.class).values());
        List<Object> exceptionHandlerBeans = new ArrayList<>(
                applicationContext.getBeansOfType(ExceptionMapper.class).values());
        endpoint.setProviders(exceptionHandlerBeans);
        endpoint.setServiceBean(apiBeans);
        endpoint.setFeatures(singletonList(beanValidationFeature));
        return endpoint.create();
    }

    @Autowired
    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @Autowired
    public void setProvider(JacksonJsonProvider provider) {
        this.provider = provider;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setBeanValidationFeature(BeanValidationFeature beanValidationFeature) {
        this.beanValidationFeature = beanValidationFeature;
    }
}