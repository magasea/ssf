package com.shellshellfish.aaas.risk.utils;

import org.apache.commons.logging.LogFactory;
import org.apache.juli.logging.Log;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.shellshellfish.aaas.risk.model.dao.OptionItem;

@Component
public class String2OptionItemConverter implements Converter<String, OptionItem>{

    @Override
    public OptionItem convert(String s) {
        return new OptionItem();
    }
}