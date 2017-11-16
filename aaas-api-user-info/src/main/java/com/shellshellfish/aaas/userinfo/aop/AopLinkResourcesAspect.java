package com.shellshellfish.aaas.userinfo.aop;

import com.shellshellfish.aaas.userinfo.dao.service.LinksDaoService;
import com.shellshellfish.aaas.userinfo.service.LinkService;
import com.shellshellfish.aaas.userinfo.util.UserInfoUtils;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AopLinkResourcesAspect {
    
    Logger logger = LoggerFactory.getLogger(AopLinkResourcesAspect.class);
    
    @Autowired
    LinkService linkService;
    

    @Around("@annotation(com.shellshellfish.aaas.userinfo.aop.AopLinkResources) && execution(public * *"
        + "(..))")
    public Object time(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object value;
        try {
            value = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            long duration = System.currentTimeMillis() - start;
            logger.info(
                    "{}.{} took {} ms",
                    proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                    proceedingJoinPoint.getSignature().getName(),
                    duration);
        }
        logger.info("{}",value);
        ResponseEntity<?> entity = (ResponseEntity) value;
        Map<String, Object> entityMap = (Map)entity.getBody();
        if(!entityMap.containsKey("_links")){
            entityMap.put("_links", "");
        }
        logger.info("{}",entityMap.get("_links"));
        Map<String, String> cond = new HashMap<>();
        if (proceedingJoinPoint.getSignature().getName().contains("getUserBaseInfo")){
            cond.put("requestName","userInfo");
        }else{
            return value;
        }
        Map<String, Object> result = linkService.getLinksForRequest(cond);
        Map<String, Object> linkinfo = new HashMap<>();
        linkinfo.put("_links", result);
        entityMap.put("_links", linkinfo);
        logger.info("{}",entity);
        return value;
    }
}