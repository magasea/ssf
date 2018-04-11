package com.shellshellfish.aaas.assetallocation.util;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformUtil {
	public static Map<String, Object> getMaxMinValue(List param) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (!CollectionUtils.isEmpty(param)) {
			result.put("maxValue", Collections.max(param));
			result.put("minValue", Collections.min(param));
		}
		return result;
	}
}
