package org.project.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class LoginController {

	@GetMapping("/login")
	public ModelAndView loginUser(Model model) {
		ModelAndView mav = new ModelAndView("login");
		String  av="0";
		return mav;

	}
	
	@GetMapping("/home")
	public ModelAndView home(Model model) {
		ModelAndView mav = new ModelAndView("home");
		String  av="0";
		return mav;

	}

}
