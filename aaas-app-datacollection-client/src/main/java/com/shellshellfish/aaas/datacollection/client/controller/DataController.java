package com.shellshellfish.aaas.datacollection.client.controller;


import com.shellshellfish.aaas.datacollection.client.model.DailyFunds;
import com.shellshellfish.aaas.datacollection.client.model.vo.FundsQuery;
import com.shellshellfish.aaas.datacollection.client.service.DataCollectionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DataController {


	@Autowired
	DataCollectionService dataCollectionService;

	@RequestMapping(value = "/queryFunds", method = RequestMethod.POST)
	public ResponseEntity<?> queryFunds(@RequestBody FundsQuery fundsQuery)
			throws Exception {
		List<DailyFunds> dailyFunds = dataCollectionService.getDailyFunds(fundsQuery);
		ResponseEntity responseEntity = new ResponseEntity(dailyFunds, HttpStatus.OK);
		return responseEntity;
	}

}
