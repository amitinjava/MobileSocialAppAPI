package com.edspread.mobileapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edspread.mobileapp.constants.MobileAppConstant;
import com.edspread.mobileapp.dao.GroupDao;
import com.edspread.mobileapp.dao.UserDao;
import com.edspread.mobileapp.dto.ChannelDto;
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
	
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public @ResponseBody ResponseData getGroups(@RequestParam String groupname, @RequestParam String owneremail) {
		ResponseData rd= new ResponseData();
		User usr = userdao.findUserByEmail(owneremail);
		GroupDto group = groupdao.getGroupMembers(groupname, usr);
		if(group != null && group.getId() != null){
			rd.data = group;
		}else{
			rd.data = false;
			String errors[] = {"groupname/owneremail does not exist"};
			rd.errors = errors;
		}
		return rd;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public @ResponseBody ResponseData deleteGroups(@RequestBody GroupDto group) {
		ResponseData rd= new ResponseData();
		User usr = userdao.findUserByEmail(group.getOwneremail());
		int status = groupdao.deleteGroupMembers(group.getName(), usr);
		if(status == 1){
			String messages[] = {"successfully deleted."};
			rd.messages = messages;
			rd.data = true;
		}else{
			rd.data = false;
			String errors[] = {"Failed"};
			rd.errors = errors;
		}
		
		return rd;
	}
	
	@RequestMapping(value = "/channel", method = RequestMethod.POST)
	public @ResponseBody ResponseData addXmessage(@RequestBody ChannelDto channel) {
		ResponseData rd= new ResponseData();
		int sequenceNumber = 0;
		try{
			if(channel.getxMsgId() ==  null){
				// New Xmessage 
				// 1) create new xmessage
				// 2) create message with new sequence
				User usr = userdao.findUserById(channel.getFrom());
				if(usr == null){
					rd.data = false;
					String errors[] = {"Invalid Sender id"};
					rd.errors = errors;
					return rd;
				}else{
					channel.setUserId(usr.getId());
					int xmesssageId = groupdao.addChannel(channel, usr);
					channel.setxMsgId(xmesssageId);
				}
			}else{
				sequenceNumber = groupdao.getMaxMessageSequence(channel.getxMsgId());
			}
			channel.setSequenceNo(sequenceNumber);
			channel.setHttpaPath(MobileAppConstant.XMSG_SERVERHTTPPATH);
			/*httpaPath = 
			xmsgId=101
			name=test
			userId=100*/
			rd.data = channel;
		}catch (Exception e) {
			String errors[] = { e.getMessage() };
			rd.errors = errors;
		}
		return rd;
	}
	
	@RequestMapping(value = "/details/{userid}", method = RequestMethod.GET)
	public @ResponseBody ResponseData getGroupsByUserId(@PathVariable Integer userid) {
		ResponseData rd= new ResponseData();
		GroupDto group = groupdao.findGroupDetailsByUserId(userid);
		if(group != null && group.getId() != null){
			rd.data = group;
		}else{
			rd.data = false;
			String errors[] = {"userId does not exist"};
			rd.errors = errors;
		}
		return rd;
	}

}
