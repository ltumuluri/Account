package org.uftwf.account.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by xyang on 4/27/17.
 */
@Configuration
@EnableWebMvc
@ComponentScan("org.uftwf.account.controller")
public class WebConfig implements WebMvcConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {

        LOGGER.info("getInternalResourceViewResolver(): Configure Resource View resolver for Account resources." + "\r\n");

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/resources/layout/");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        LOGGER.info("addResourceHandlers(): Add resources to the Resource Handler Registry." + "\r\n");

        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/WEB-INF/resources/");
        registry.addResourceHandler("/styles/**")
                .addResourceLocations("/WEB-INF/resources/styles/");
        registry.addResourceHandler("/scripts/**")
                .addResourceLocations("/WEB-INF/resources/scripts/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("/WEB-INF/resources/images/");
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("/WEB-INF/resources/images/fonts/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {


        LOGGER.info("configureDefaultServletHandling(): "
                + " Configure Default Servlet Handler configurer. Configurer: \"" + configurer.getClass().getName() + "\""
                + "\r\n");

        configurer.enable();
    }
}
