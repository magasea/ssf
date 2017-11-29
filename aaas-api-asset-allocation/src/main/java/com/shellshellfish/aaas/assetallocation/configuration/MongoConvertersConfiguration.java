package com.shellshellfish.aaas.assetallocation.configuration;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;

//@Configuration
public class MongoConvertersConfiguration {

    @Resource(name = "defaultConversionService")
    private GenericConversionService genericConversionService;

//    @Bean
//    public String2OptionItemConverter string2MongoUserConverter(){
//        String2OptionItemConverter string2MongoUserConverter = new String2OptionItemConverter();
//        genericConversionService.addConverter(string2MongoUserConverter);
//        return string2MongoUserConverter;
//    }

}