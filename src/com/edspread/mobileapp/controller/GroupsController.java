package com.edspread.mobileapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edspread.mobileapp.dao.GroupDao;
import com.edspread.mobileapp.dao.UserDao;
import com.edspread.mobileapp.dto.GroupDto;
import com.edspread.mobileapp.dto.ResponseData;
import com.edspread.mobileapp.entity.User;

@Controller
@RequestMapping("/group")
public class GroupsController {
	
	@Autowired
	GroupDao groupdao;
	@Autowired
	UserDao userdao;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseData addGroups(@RequestBody GroupDto group) {
		ResponseData rd= new ResponseData();
		try{
		User usr = userdao.findUserByEmail(group.getOwneremail());
		if(usr != null){
		Integer groupId = groupdao.saveGroupMembers(group, usr);
		group.setId(groupId);
		rd.data = group;
		}else{
			String errors[] = {"Owner does not exist"};
			rd.errors = errors;
		}
		}catch(Exception e){
			String errors[] = {e.getMessage()};
			rd.errors = errors;
		}
		return rd;
	}
	
	@RequestMapping(value = "/get/{groupid}", method = RequestMethod.GET)
	public @ResponseBody ResponseData getGroups(@PathVariable("groupid") Integer groupId) {
		ResponseData rd= new ResponseData();
		
		GroupDto group = groupdao.getGroupMembers(groupId);
		rd.data = group;
		return rd;
	}

}
