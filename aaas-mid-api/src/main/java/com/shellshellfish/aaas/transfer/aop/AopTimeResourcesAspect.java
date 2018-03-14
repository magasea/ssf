package com.shellshellfish.aaas.transfer.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AopTimeResourcesAspect {

	Logger logger = LoggerFactory.getLogger(AopTimeResourcesAspect.class);

	@Around("@annotation(com.shellshellfish.aaas.transfer.aop.AopTimeResources) && execution(public * *" + "(..))")
	public Object time(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long startTime;
		long endTime;
		Object obj;
		try {
			// 获取开始时间
			startTime = System.currentTimeMillis();
			// 获取返回结果集
			obj = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
			// 获取方法执行时间
			endTime = System.currentTimeMillis();
		} catch (Throwable t) {
			// 当方法中报异常时，会抛出这里的异常，
			logger.error("mid api time error .", t);
			throw new Exception(t);
		}
		String classAndMethod = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "."
				+ proceedingJoinPoint.getSignature().getName();
		logger.info("执行 " + classAndMethod + " 耗时为：" + (endTime - startTime) + "ms");
		return obj;
	}
}
