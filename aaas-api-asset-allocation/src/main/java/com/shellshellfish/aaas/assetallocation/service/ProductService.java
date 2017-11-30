package com.shellshellfish.aaas.assetallocation.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.assetallocation.model.RiskControl;
import com.shellshellfish.aaas.assetallocation.util.CollectionResourceWrapper;
import com.shellshellfish.aaas.assetallocation.util.NameValuePair;

@Service
public class ProductService {
	
	public NameValuePair<String, Double> calcExpectedAnnualizedReturn() {
		return new NameValuePair<>("预期年化收益", 0.105d);
	}
	
	public NameValuePair<String, Double> calcExpectedMaxPullback() {
		return new NameValuePair<>("预期最大回撤", 0.035);
	} 
	
//	public CollectionResourceWrapper<List<RiskControl>> calcRiskControls() {
//		CollectionResourceWrapper<List<RiskControl>> resource = new CollectionResourceWrapper<>();
//		resource.setItems(Arrays.asList(new RiskControl("历史最大回撤",  -0.0189d, -0.0455d), 
//										new RiskControl("股灾1",  -0.0189d, -0.0455d), 
//										new RiskControl("股灾2",  -0.0189d, -0.0455d), 
//										new RiskControl("熊市1",  -0.0189d, -0.0455d)));
//		
//		return resource;
//	}
//	
}
