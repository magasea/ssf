package com.shellshellfish.fundcheck.configuration;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chenwei on 2018- 三月 - 07
 */
@Configuration
public class WebConfig {

  @Bean
  public TomcatEmbeddedServletContainerFactory tomcatEmbedded() {

    TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

    tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
      if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
        //-1 means unlimited
        ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
      }
    });

    return tomcat;

  }
}
