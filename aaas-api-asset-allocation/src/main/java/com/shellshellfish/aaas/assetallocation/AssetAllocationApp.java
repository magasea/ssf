package com.shellshellfish.aaas.assetallocation;


import com.shellshellfish.aaas.assetallocation.configuration.Properties;
import io.grpc.Server;
import java.io.IOException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@ComponentScan
@SpringBootApplication(scanBasePackages = {"com.shellshellfish.aaas.assetallocation", "com.shellshellfish.aaas.assetallocation.util"})
@EnableScheduling
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
@EnableConfigurationProperties({Properties.class})
@MapperScan("com.shellshellfish.aaas.assetallocation.mapper")

public class AssetAllocationApp {

    private static Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run
                (AssetAllocationApp.class, args);
        server = (Server) configurableApplicationContext.getBean("server");
        server.start();

        blockUntilShutdown();
    }

    private static void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }

    }
}

