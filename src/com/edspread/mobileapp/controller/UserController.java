/**
 * 
 */
package com.edspread.mobileapp.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.edspread.mobileapp.constants.MobileAppConstant;
import com.edspread.mobileapp.dao.UserDao;
import com.edspread.mobileapp.dao.UserFriendsDao;
import com.edspread.mobileapp.dto.ResponseData;
import com.edspread.mobileapp.dto.UserDto;
import com.edspread.mobileapp.dto.XmppUserDTO;
import com.edspread.mobileapp.entity.User;
import com.edspread.mobileapp.entity.UserDetail;
import com.edspread.mobileapp.entity.UserFriends;
import com.edspread.mobileapp.utils.AppUtillty;
import com.edspread.mobileapp.utils.DateUtil;
import com.edspread.mobileapp.utils.JDBCUtill;
import com.edspread.mobileapp.utils.PasswordGenerator;
import com.edspread.mobileapp.utils.SessionUtil;

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
	UserFriendsDao userFriendsDao;
	@Autowired
	AppUtillty appUtillty;

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody ResponseData login(@RequestBody UserDto user) {
		ResponseData rd= new ResponseData();
		if(user.email == null || user.password == null){
			rd.data = false;
			String[] errors = {"email and password are mandatory fields"};
			rd.errors = errors;
			return rd;
		}
		UserDto udo = userdao.login(user.email, user.password);
		
		if (udo == null) {
			rd.data = false;
			String[] errors = {"Wrong Login Credentials"};
			rd.errors = errors;
		} else {
			rd.data = udo.active;
			if(!udo.active){
				String[] errors = {"Your account is not activated."};
				rd.errors = errors;
			}else if(udo.active){
				String[] messages = {"Successfully Logged-In."};
				rd.messages = messages;
				rd.data = udo.id;
			}
		}
		return rd;
	}

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody ResponseData register(@RequestBody UserDto user) {
		ResponseData rd= new ResponseData();
		if(user.email == null || user.password == null){
			rd.data = false;
			String[] errors = {"email and password are mandatory fields"};
			rd.errors = errors;
			return rd;
		}
		user.active = false;
		String code = appUtillty.getValidationCode(6);
		user.registrationCode = code;
		String encryptedPassword = PasswordGenerator.encryptPassword(user.password);
		user.password = encryptedPassword;
		
		int status = 0;
		try {
			status = userdao.register(user);
		}catch (DuplicateKeyException dke){
			User duser = userdao.getDeactivateUser(user);
			if(duser != null){
				try {
					int upstatus = userdao.updateRegistrationCode(user.getEmail(),  code, encryptedPassword);
					
					final String body = "Validation Code:" + code;
					final List<String> toList = new ArrayList<String>();
					toList.add(user.email);
					
					appUtillty.sendMail2Users(toList, body, "info@ttmac.com",
							"Edspread", "Validation Code", null);
				} catch (Exception  e) {
					e.printStackTrace();
				}
				rd.data = true;
				String[] messages = {"Successfully Registered"};
				rd.messages = messages;
				return rd;
			}
			rd.data = false;
			String[] errors = {"Supplied email exists in the System."};
			rd.errors = errors;
			return rd;
		}
		
		if (status == 1) {
			try {
				final String body = "Validation Code:" + code;
				final List<String> toList = new ArrayList<String>();
				toList.add(user.email);
				
				appUtillty.sendMail2Users(toList, body, "info@ttmac.com",
						"Edspread", "Validation Code", null);
			} catch (Exception  e) {
				e.printStackTrace();
			}
			rd.data = true;
			String[] messages = {"Successfully Registered"};
			rd.messages = messages;
			return rd;
		} else {
			rd.data = false;
			return rd;
		}

	}

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/activate", method = RequestMethod.POST)
	public @ResponseBody ResponseData activate(@RequestBody UserDto user) {
		ResponseData rd= new ResponseData();
		if(user.email == null || user.registrationCode == null){
			rd.data = false;
			String[] errors = {"email and registrationCode are mandatory fields"};
			rd.errors = errors;
			return rd;
		}
		User usr = userdao.activate(user);
		try {
			if(usr != null){
			registerUserOnXmpp(usr);
			addFriends(usr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (usr != null && usr.getId() != null) {
			List<String> emails = userFriendsDao.getFriendsEmail(user.email);
			rd.data = emails;
			String messages[] = {"User is Activated"};
			rd.messages = messages;
			return rd;
		} else {
			rd.data = false;
			String errors[] = {"User is not Activated"};
			rd.errors = errors;
			return rd;
		}

	}

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public @ResponseBody ResponseData forgotPassword(@RequestBody UserDto user) {
		ResponseData rd= new ResponseData();
		if(user.email == null){
			rd.data = false;
			String[] errors = {"email is mandatory field"};
			rd.errors = errors;
			return rd;
		}
		UserDto userDto = userdao.forgotPassword(user.email);
		if (userDto != null) {
			if(userDto.active){
			try {
				String decryptPassword = PasswordGenerator.decryptPassword(userDto.password);
				final String body = "Password: " + decryptPassword;
				final List<String> toList = new ArrayList<String>();
				toList.add(user.email);
				
				appUtillty.sendMail2Users(toList, body, "info@ttmac.com",
						"Edspread", "Password", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			rd.data = true;
			String messages[] = {"password is sent to your registered email"};
			rd.messages = messages;
			}else{
				rd.data = false;
				String errors[] = {"Your account is not activated to get password in email."};
				rd.errors = errors;

			}
		} else {
			rd.data = false;
			String errors[] = {"Wrong Email"};
			rd.messages = errors;
		}
		return rd;
	}
	
	private int registerUserOnXmpp(User user) throws Exception {
		String createdAt = "" + DateUtil.getTimeInLong();
		//password = user.getPhone().substring(0, 5);
		String decryptPassword = PasswordGenerator.decryptPassword(user.password);
		//String encryptedPassword = PasswordGenerator.encryptPassword(password);
		XmppUserDTO xmppUserDTO = new XmppUserDTO();
		//xmppUserDTO.setUserName(user.getPhone());
		xmppUserDTO.setUserName(user.getEmail().replace("@", "_"));
		xmppUserDTO.setPlainPassword(decryptPassword);
		xmppUserDTO.setEncryptedPassword(user.password);
		//xmppUserDTO.setName(user.getPhone());
		xmppUserDTO.setName(user.getEmail());
		xmppUserDTO.setEmail(user.getEmail());
		xmppUserDTO.setCreatedAt(createdAt);
		xmppUserDTO.setUpdatedAt(createdAt);
		return JDBCUtill.addXMPPUser(xmppUserDTO);
	}
	
	private void addFriends(User user) {
			List<User> userContactList = userFriendsDao.getFriendIds(user.getEmail());
			UserFriends ufrnd;
			Date currentDate = DateUtil.getTodayDate();
			if(userContactList != null){
				for (User usr : userContactList) {
					ufrnd  = new UserFriends();
					ufrnd.setUserId(user.getId());
					ufrnd.setFriendId(usr.getId());
					ufrnd.setUpdatedAt(currentDate);
					ufrnd.setCreatedAt(currentDate);
					userFriendsDao.saveFriend(ufrnd);
					
				}
				
				for (User usr : userContactList) {
					ufrnd  = new UserFriends();
					ufrnd.setUserId(usr.getId());
					ufrnd.setFriendId(user.getId());
					ufrnd.setUpdatedAt(currentDate);
					ufrnd.setCreatedAt(currentDate);
					userFriendsDao.saveFriend(ufrnd);
					
				}
			}
	}
	
	@RequestMapping(value = "/friends", method = RequestMethod.GET)
	public @ResponseBody ResponseData friends(@RequestParam String email) {
		List<String> emails = userFriendsDao.getFriendsEmail(email);
		ResponseData rd= new ResponseData();
		rd.data = emails;
		return rd;
	}
	
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public @ResponseBody ResponseData getUserdetail(@RequestParam String email) {
		UserDetail userDetail = userdao.getUserDetailsByEmail(email);
		ResponseData rd= new ResponseData();
		if(userDetail != null){
			rd.data = userDetail;
		}else{
			rd.data = false;
			String errors[] = {"email does not exist"};
			rd.errors = errors;
		}
		return rd;
	}
	
	@RequestMapping(value = "/details", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	public @ResponseBody ResponseData addUserdetail(@RequestParam(value="inputFile" , required=false) MultipartFile inputFile,
			@RequestParam String name,@RequestParam Integer mobile, @RequestParam String email) {
		ResponseData urd = upload(inputFile);
		
		UserDto user = new UserDto();
		user.setName(name);
		user.setEmail(email);
		user.setMobile(mobile);
		if(urd.data != null){
			user.setProfilePix(urd.data.toString());
		}
		int status = userdao.addUserDetails(user);
		ResponseData rd= new ResponseData();
		if(status == 1){
		rd.data = true;
		String messages[] = {"successfully Added."};
		rd.messages = messages;
		}else{
			rd.data = false;
			String errors[] = {"email does not exist."};
			rd.errors = errors;
		}
		//rd.data = emails;
		return rd;
	}
	
	
	
	public ResponseData upload(MultipartFile inputFile) {
		ResponseData rd= new ResponseData();
		String contextPath = SessionUtil.getContextPath();
		
		
		
		String uploadDir = contextPath + File.separator + "upload" + File.separator + DateUtil.getFormattedDateTime();
		File uploadDirFp = new File(uploadDir);
		if (!uploadDirFp.exists()) {
            if (!uploadDirFp.mkdirs()) {
               System.out.println("Failed to create directory");
               String[] errors = {"Failed to create directory"};
               rd.errors = errors;
                return rd;
            }
        }
		
		String uploadedFileServerPath = null;
		//Map<String,String> files = new HashMap<String,String>();
		if(inputFile != null){
		String inputFileName =  inputFile.getOriginalFilename();
		uploadedFileServerPath = "upload" + "/" + DateUtil.getFormattedDateTime() + "/"+ inputFileName;
		String actualFilePath = SessionUtil.getContextPath() + File.separator + "upload" + File.separator + DateUtil.getFormattedDateTime()+ File.separator + inputFileName;
		File destFile = new File(actualFilePath);
		try {
			FileUtils.copyFile(multipartToFile(inputFile), destFile);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		uploadedFileServerPath = MobileAppConstant.SERVERHTTPPATH + uploadedFileServerPath;
		//files.put("inputFile",uploadedFileServerPath);
		}
		rd.data = uploadedFileServerPath;
		return rd;

	}
	
	public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException 
	{
	    File convFile = new File( multipart.getOriginalFilename());
	    multipart.transferTo(convFile);
	    return convFile;
	}

}
