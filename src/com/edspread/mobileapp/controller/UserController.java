/**
 * 
 */
package com.edspread.mobileapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edspread.mobileapp.dao.UserDao;
import com.edspread.mobileapp.dto.UserDto;
import com.edspread.mobileapp.utils.AppUtillty;
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
	@Autowired
	AppUtillty appUtillty;

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
		String code = appUtillty.getValidationCode(6);
		user.registrationCode = code;
		int status = userdao.register(user);
		
		if (status == 1) {
			try {
				final String body = "Validation Code:" + code;
				final List<String> toList = new ArrayList<String>();
				toList.add(user.email);
				
				appUtillty.sendMail2Users(toList, body, "info@ttmac.com",
						"Edspread", "Validation Code", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		UserDto userDto = userdao.forgotPassword(user.email);
		if (userDto != null) {
			
			try {
				final String body = "Password: " + userDto.password;
				final List<String> toList = new ArrayList<String>();
				toList.add(user.email);
				
				appUtillty.sendMail2Users(toList, body, "info@ttmac.com",
						"Edspread", "Password", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return "password is sent to your registered email";
		} else {
			return "Wrong Email";
		}
	}

}
