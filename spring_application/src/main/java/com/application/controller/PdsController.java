package com.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.application.service.PdsService;

@Controller
@RequestMapping("/pds")
public class PdsController {
	
	@Autowired
	private PdsService pdsService;

}
