package com.shellshellfish.aaas.controller;

import com.shellshellfish.aaas.model.HistoryPerformance;
import com.shellshellfish.aaas.model.HistoryPerformanceResource;
import com.shellshellfish.aaas.service.HistoryPerformanceService;
import com.shellshellfish.aaas.util.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {

	@RequestMapping("/")
	String home(ModelMap modal) {
		modal.addAttribute("title","CRUD Example");
		return "index";
	}

	@RequestMapping("/asdf")
	String dummy(ModelMap modal) {
		modal.addAttribute("title","CRUD Example");
		return "list";
	}

	@RequestMapping("/partials/{page}")
	@ResponseBody
	String partialHandler(@PathVariable("page") final String page) {
		return page;
	}

}
