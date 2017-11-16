package com.shellshellfish.aaas.risk.configuration;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;

import com.shellshellfish.aaas.risk.util.String2OptionItemConverter;

//@Configuration
public class MongoConvertersConfiguration {

    @Resource(name = "defaultConversionService")
    private GenericConversionService genericConversionService;

    @Bean
    public String2OptionItemConverter string2MongoUserConverter(){
        String2OptionItemConverter string2MongoUserConverter = new String2OptionItemConverter();
        genericConversionService.addConverter(string2MongoUserConverter);
        return string2MongoUserConverter;
    }

}