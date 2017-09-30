/**
 * 
 */
package com.edspread.mobileapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edspread.mobileapp.dao.UserDao;
import com.edspread.mobileapp.dto.UserDto;
import com.edspread.mobileapp.utils.RandomCode;
import com.edspread.mobileapp.utils.SendEmail;

/**
 * @author Amit.Kumar1
 *
 */
@Controller
@RequestMapping("/user")

public class UserController {
	@Autowired
	UserDao userdao;

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
	public @ResponseBody String login(@RequestBody UserDto user) {
		Integer id = userdao.login(user.email, user.password);
		if (id != null) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
	public @ResponseBody String register(@RequestBody UserDto user) {
		user.active = false;
		String code = RandomCode.generate();
		user.registrationCode = code;
		int status = userdao.register(user);
		try {
			SendEmail se = new SendEmail();
			 se.send(user.email, user.registrationCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (status == 1) {
			return "true";
		} else {
			return "false";
		}

	}

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/activate", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
	public @ResponseBody String activate(@RequestBody UserDto user) {
		System.out.println(user.email);
		Integer id = userdao.activate(user);
		if (id != null) {
			return "User is Activated";
		} else {
			return "User is not Activated";
		}

	}

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
	public @ResponseBody String forgotPassword(@RequestBody UserDto user) {
		System.out.println(user.email);
		Integer id = userdao.forgotPassword(user.email);
		if (id != null) {
			return "password is sent to your registered email";
		} else {
			return "Wrong Email";
		}
	}

}
