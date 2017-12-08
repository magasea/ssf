package com.shellshellfish.aaas.account.aop;

import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.shellshellfish.aaas.account.utils.PageWrapper;

@Aspect
@Component
@Order(-5)
public class AopResourcesAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(AopResourcesAspect.class);

	@Around("@annotation(com.shellshellfish.aaas.account.aop.AopPageResources) && execution(public * *" + "(..))")
	public Object time(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object value;
		try {
			value = proceedingJoinPoint.proceed();
		} catch (Throwable throwable) {
			throw throwable;
		} finally {
			long duration = System.currentTimeMillis() - start;
			LOGGER.info("{}.{} took {} ms", proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
					proceedingJoinPoint.getSignature().getName(), duration);
		}
		LOGGER.info("{}", value);
		ResponseEntity<PageWrapper<?>> entity = (ResponseEntity<PageWrapper<?>>) value;
		PageWrapper<?> pageEntity = entity.getBody();
		Map<String, Object> _link = (Map) pageEntity.get_links();
		if (_link == null) {
			pageEntity.set_links(new HashMap<String, Object>());
		}
		Map<String, Object> nextPage = new HashMap<String, Object>();
		Map<String, Object> linkMap = pageEntity.get_links();
		Map<String, Object> selfMap = (Map<String, Object>) linkMap.get("self");
		String url = (String) selfMap.get("href");
		int size = pageEntity.getSize();
		int number = pageEntity.getNumber();
		Sort sort = pageEntity.getSort();
		if (pageEntity.isHasNextPage()) {
			String nextUrl = "";
			if (sort != null) {
				nextUrl = url + "?page=" + number + "&size=" + size + "&sort=" + sort;
			} else {
				nextUrl = url + "?page=" + number + "&size=" + size;
			}
			nextPage.put("next", nextUrl);
			pageEntity.set_links(nextPage);
		}

		if (!pageEntity.isFirstPage()) {
			String prevUrl = "";
			int totalPages = pageEntity.getTotalPages();
			if (number - 1 > totalPages) {
				number = totalPages - 1;
			} else{
				number = number - 2;
			}
			if (sort != null) {
				prevUrl = url + "?page=" + number + "&size=" + size + "&sort=" + sort;
			} else {
				prevUrl = url + "?page=" + number + "&size=" + size;
			}
			nextPage.put("prev", prevUrl);
			pageEntity.set_links(nextPage);
		}

		return value;
	}
}
