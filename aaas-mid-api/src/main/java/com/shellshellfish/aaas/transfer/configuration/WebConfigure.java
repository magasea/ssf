package com.shellshellfish.aaas.transfer.configuration;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.service.MidApiService;
import com.shellshellfish.aaas.transfer.service.impl.MidApiServiceImpl;


@Configuration
public class WebConfigure implements ServletContextInitializer, EmbeddedServletContainerCustomizer{
	private final Logger log = LoggerFactory.getLogger(WebConfigure.class);
	
	@Autowired
	private Environment env;
	
	
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		 MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);

	        mappings.add("html", "text/html;charset=utf-8");

	        mappings.add("json", "text/html;charset=utf-8");
	        container.setMimeMappings(mappings);

	        // When running in an IDE or with ./mvnw spring-boot:run, set location of the static web assets.
	        setLocationForStaticAssets(container);
		
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
      
		if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
        }

        log.info("Web application fully configured");
		
	}
	
	private void setLocationForStaticAssets(ConfigurableEmbeddedServletContainer container) {
        File root;
        String prefixPath = resolvePathPrefix();
        if (env.acceptsProfiles("prod")) {
            root = new File(prefixPath + "target/www/");
        } else {
            root = new File(prefixPath + "src/main/webapp/");
        }
        if (root.exists() && root.isDirectory()) {
            container.setDocumentRoot(root);
        }
    }
	
	 /**
     *  Resolve path prefix to static resources.
     */
    private String resolvePathPrefix() {
        String fullExecutablePath = this.getClass().getResource("").getPath();
        String rootPath = Paths.get(".").toUri().normalize().getPath();
        String extractedPath = fullExecutablePath.replace(rootPath, "");
        int extractionEndIndex = extractedPath.indexOf("target/");
        if(extractionEndIndex <= 0) {
            return "";
        }
        return extractedPath.substring(0, extractionEndIndex);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    
    @Bean
    public MidApiService midApiService(){
        return new MidApiServiceImpl();
    }


}
