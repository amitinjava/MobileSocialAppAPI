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

@Controller
@RequestMapping("/group")
public class GroupsController {
	
	@Autowired
	GroupDao groupdao;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseData addGroups(@RequestBody GroupDto group) {
		ResponseData rd= new ResponseData();
		
		Integer groupId = groupdao.saveGroupMembers(group);
		group.setId(groupId);
		rd.data = group;
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
