package com.shellshellfish.aaas.finance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shellshellfish.aaas.finance.model.dto.HistoryList;
import com.shellshellfish.aaas.finance.service.GroupDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author pierre
 * 17-12-21
 */

@Service
public class GroupDetailsServiceImpl implements GroupDetailsService {


	@Value("${api-data-manager-url}")
	private String dataManagerBaseUrl;

	@Autowired
	RestTemplate restTemplate;


	/**
	 * 历史净值
	 * code:基金代码
	 * period: 1: 3month,2: 6month,3: 1year,4: 3year
	 * @param methodUrl
	 * @param params
	 * @return
	 */
	@Override
	public List<HistoryList> getHistoryList(String methodUrl, Map params) {
		String origin = connectDataManager(methodUrl,params);
		if(origin != null){
			return prepareHistoryList(origin);
		}

		return null;
	}

	@Override
	public String connectDataManager(String methodUrl, Map params) {
		ResponseEntity<String> entity =
				restTemplate.getForEntity(prepareParameters(dataManagerBaseUrl + methodUrl, params), String.class);
		if (entity != null && entity.getStatusCode().equals(HttpStatus.OK))
			return entity.getBody();

		return null;
	}

	private String prepareParameters(String url, Map<String, String> params) {
		url += "?";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			url += entry.getKey() + "=" + entry.getValue() + "&";
		}
		return url.substring(0, url.lastIndexOf("&"));

	}


	/**
	 * <Strong>
	 *
	 *    ### 各位，请注意，这里是坑！！！ ###
	 * </Strong>
	 *
	 *
	 * 拆分历史净值数据拼接成HistoryList
	 * 格式如下：
	 * {
	 * "period": "1",
	 * "code": "000001.OF",
	 * "historylist": [
	 * {
	 * "historydetail": "date1|||unit net1|||accum1|||day scale1"
	 * }
	 * ]
	 * }
	 * @param
	 * @return
	 */
	private List prepareHistoryList(String origin) {

		JSONObject originJson = JSONObject.parseObject(origin);

		JSONArray originHistoryList = originJson.getJSONArray("historylist");

		if (originHistoryList == null || originHistoryList.isEmpty()) {
			return null;
		}

		List<HistoryList> historyList = new ArrayList<>(originHistoryList.size());
		for (int i = 0; i < originHistoryList.size(); i++) {
			String originParams = originHistoryList.getJSONObject(i).getString("historydetail");
			String[] params =  originParams.replace("|||",",").split(",");
			if(params ==null || params.length != 4 ){
				continue;
			}

			historyList.add(new HistoryList(params));

		}
		return historyList;
	}

}
