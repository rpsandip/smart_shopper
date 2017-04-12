package com.hk.ss.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Preconditions;
import com.hk.ss.dto.DUser;

@Controller
public class UserController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView signup(HttpServletRequest request, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView("login");
		DUser userLogin = new DUser();
		modelAndView.addObject("userLogin", userLogin);
		return modelAndView;

	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register(HttpServletRequest request, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView("register");
		DUser userLogin = new DUser();
		modelAndView.addObject("userLogin", userLogin);
		return modelAndView;

	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public void userRegister(@ModelAttribute final DUser dUser) {
		Preconditions.checkNotNull(dUser.getPassword(), "password can not be null");
		Preconditions.checkNotNull(dUser.getEmail(), "email id can not be null");
		System.out.println("User Register");
	}
	
	@RequestMapping(value = "/user/signin", method = RequestMethod.POST)
	public void userSignup(@ModelAttribute final DUser dUser) {
		Preconditions.checkNotNull(dUser.getPassword(), "password can not be null");
		Preconditions.checkNotNull(dUser.getEmail(), "email id can not be null");
		System.out.println("test");
	}
}
