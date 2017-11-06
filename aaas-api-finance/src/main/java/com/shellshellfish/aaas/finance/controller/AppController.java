package com.shellshellfish.aaas.finance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
