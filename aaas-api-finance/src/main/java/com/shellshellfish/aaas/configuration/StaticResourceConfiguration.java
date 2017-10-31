package com.shellshellfish.aaas.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
//
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer.favorPathExtension(true)
//                .useJaf(false)
//                .ignoreAcceptHeader(true)
//                .mediaType("html", MediaType.TEXT_HTML)
//                .mediaType("json", MediaType.APPLICATION_JSON)
//                .defaultContentType(MediaType.TEXT_HTML);
//    }

//    @Bean
//    public ViewResolver contentNegotiatingViewResolver(
//            ContentNegotiationManager manager) {
//
//        List< ViewResolver > resolvers = new ArrayList< ViewResolver >();
//
//        InternalResourceViewResolver r1 = new InternalResourceViewResolver();
//        r1.setPrefix("/WEB-INF/pages/");
//        r1.setSuffix(".jsp");
//        r1.setViewClass(JstlView.class);
//        resolvers.add(r1);
//
//        JsonViewResolver r2 = new JsonViewResolver();
//        r1.setSuffix(".json");
//        resolvers.add(r2);
//
//        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
//        resolver.setViewResolvers(resolvers);
//        resolver.setContentNegotiationManager(manager);
//        return resolver;
//
//    }
//
//    public class JsonViewResolver implements ViewResolver {
//        public View resolveViewName(String viewName, Locale locale)
//                throws Exception {
//            MappingJackson2JsonView view = new MappingJackson2JsonView();
//            view.setPrettyPrint(true);
//            return view;
//        }
//    }

}