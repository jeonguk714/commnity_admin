package com.greenart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManageController {
	@GetMapping("/dashboard")
	public String getDashBoard() {
		return "/dashboard/dashboard";
	}
}
