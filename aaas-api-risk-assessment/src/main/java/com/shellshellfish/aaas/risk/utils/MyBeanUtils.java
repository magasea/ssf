package com.shellshellfish.aaas.risk.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class MyBeanUtils {

	public static <A, B> List<B> convertList(List<A> sourceList, Class<B> targetClass)
			throws IllegalAccessException, InstantiationException {
		List<B> targetList = new ArrayList<>();
		for (A item : sourceList) {
			B targetItem = targetClass.newInstance();
			BeanUtils.copyProperties(item, targetItem);
			targetList.add(targetItem);
		}
		return targetList;
	}

	public static <A, B> Page<B> convertPageDTO(Pageable pageable, Page<A> pageA,
			Class<B> targetClassB) throws InstantiationException, IllegalAccessException {
		List<B> dtoList = new ArrayList<>();
		List<A> daoList = pageA.getContent();
		B targetItemB = targetClassB.newInstance();
		daoList.forEach(e -> dtoList.add((B) mapEntityIntoDTO(e, targetItemB)));
		Page<B> dtoPage = new PageImpl<B>(((List<B>) dtoList), pageable, pageA.getTotalElements());
		return dtoPage;
	}

	public static <A, B> Object mapEntityIntoDTO(A targetItemA, B targetItemB) {
		BeanUtils.copyProperties(targetItemA, targetItemB);
		return targetItemB;
	}
}
