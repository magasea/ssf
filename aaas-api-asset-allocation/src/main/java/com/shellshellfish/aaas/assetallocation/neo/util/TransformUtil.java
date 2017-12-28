package com.shellshellfish.aaas.assetallocation.neo.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformUtil {
	public static Map<String, Object> getMaxMinValue(List param) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (param != null && param.size() > 0) {
			if (param != null && param.size() > 0) {
				// double max = Collections.max(list);
				// double min = Collections.min(list);
				result.put("maxValue", Collections.max(param));
				result.put("minValue", Collections.min(param));
			}
		}
		return result;
	}
}
