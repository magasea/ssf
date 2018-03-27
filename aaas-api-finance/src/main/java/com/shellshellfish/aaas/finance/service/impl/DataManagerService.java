package com.shellshellfish.aaas.finance.service.impl;

import com.shellshellfish.aaas.common.utils.URLutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DataManagerService {
    @Value("${api-data-manager-url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;


    public Map<String,Object> getBaseLine(Long groupId, Integer peroid){
        String methodUrl ="/api/datamanager/getGroupBaseLine";
        Map params = new HashMap();
        params.put("groupId",groupId.toString());
        params.put("period",peroid.toString());

        Map result = restTemplate.getForEntity(URLutils.prepareParameters(url+methodUrl,params),Map.class).getBody();
        return result;
    }


}
