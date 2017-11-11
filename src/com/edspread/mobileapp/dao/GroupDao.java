package com.edspread.mobileapp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.edspread.mobileapp.dto.ChannelDto;
import com.edspread.mobileapp.dto.GroupDto;
import com.edspread.mobileapp.entity.Channel;
import com.edspread.mobileapp.entity.Groups;
import com.edspread.mobileapp.entity.User;
import com.edspread.mobileapp.utils.DateUtil;

public class GroupDao {
	private JdbcTemplate jdbcTemplate;  
	  
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
	    this.jdbcTemplate = jdbcTemplate;  
	}  
	
	public Integer saveGroupMembers(GroupDto gdto, User user) throws DuplicateKeyException{
		
		Date today = new Date(); 
		Number groupId = saveGroup(gdto, today, user);
		
		
		return Integer.parseInt(groupId.toString());
	}
	
	private Groups findGroupIdByNameAndOwner(String groupName, Integer ownerid){
		String sql = "SELECT * FROM groups GP WHERE GP.owner = ? AND GP.name = ?";
		Groups groups = null;
		try{
		groups =  (Groups)jdbcTemplate.queryForObject(sql, new Object[]
		        {ownerid,  groupName }, new RowMapper()
        {
            @Override
            public Groups mapRow(ResultSet rs, int rowNum) throws SQLException
            {
            	
            	Groups grps = new Groups();
            	grps.setId(rs.getInt(1));
            	//usr.setEmail(rs.getString(2));
                return grps;
            }
        });
		}catch(EmptyResultDataAccessException e){
			return groups;
		}
		return groups;
		
	}
	
	private Number saveGroup(GroupDto gdto, Date today, User usr) {
		Groups existingGroups = findGroupIdByNameAndOwner(gdto.getName(), usr.getId());
		Number groupId = null;
		if(existingGroups == null){
		SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("groups")
				.usingGeneratedKeyColumns("id");

		Map<String, Object> groupparams = new HashMap();
		groupparams.put("ID", gdto.getId());
		groupparams.put("name", gdto.getName());
		groupparams.put("owner", usr.getId());
		groupparams.put("created_at", today);
		groupparams.put("updated_at", today);
		groupparams.put("active", true);
		groupId = insert.executeAndReturnKey(groupparams);
		}else{
			groupId = existingGroups.getId();
		}
		
		
		if(gdto.getEmails() != null && !gdto.getEmails().isEmpty()){
		List<User> users = getMembersId(gdto.getEmails());
		
		
		String sql = "insert into groupsmembers values(?,?,?,?,?,?,?,?)";
		for(User user:users){
		       int status = jdbcTemplate.update(sql, new Object[]
		        {null, groupId, user.getId(), today,today,null,null, true});
		}
		}
		return groupId;
	}
	
	public List<User> getMembersId(List<String> emails){
		String sql = "select * from APIUser where email in (";
		for(String email : emails){
			sql = sql+"'"+email+"',";
			
		}
		sql = sql.substring(0, sql.length()-1);
		sql = sql +") and active=1";
		List<Map<String,Object>> usrs = null;
		List<User> userList = new ArrayList<User>();
		try{
		usrs =  jdbcTemplate.queryForList(sql, new Object[]
        {  });
		
		User usr=null;
		for(Map<String,Object> map:usrs){
			usr = new User();
			usr.setId(Integer.parseInt(map.get("ID").toString()));
			userList.add(usr);
		}
		
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		return userList;
	}

	public GroupDto getGroupMembers(String grpName, User usr) {
		String groupsql = "SELECT gs.id as groupid, au.email as email, gs.name as name FROM APIUser au, groups gs, groupsmembers gms where au.ID = gms.userid and gs.id = gms.groupid and gs.name=? and gs.owner=?";
		List<Map<String,Object>> groupsmap = null;
		GroupDto gdto = null;
		try{
			groupsmap =  jdbcTemplate.queryForList(groupsql, new Object[]
	        { grpName, usr.getId() });
			
			gdto = new GroupDto();
			gdto.setEmails(new ArrayList<String>());
			Integer grpId = null;
			String groupName=null;
			for(Map<String,Object> map:groupsmap){
				gdto.getEmails().add(map.get("email").toString());
				grpId = Integer.parseInt(map.get("groupid").toString());
				groupName = map.get("name").toString();
			}
			gdto.setId(grpId);
			gdto.setName(groupName);
			}catch(EmptyResultDataAccessException e){
				return null;
			}
		
		return gdto;
	}

	public int deleteGroupMembers(String groupname , User usr) {
		String sql = "delete from groups where name =? and owner=?";
		int status = jdbcTemplate.update(sql, new Object[]
		        {groupname,  usr.getId()});
		System.out.println(status);
		return status;
	}

	public int addChannel(ChannelDto xmessage, User usr) {
		Date today = new Date(); 
		SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("xmessage")
				.usingGeneratedKeyColumns("id");
		Map<String, Object> xmessageparams = new HashMap();
		xmessageparams.put("id", xmessage.getxMsgId());
		xmessageparams.put("name", xmessage.getTitle());
		xmessageparams.put("owner", usr.getId());
		xmessageparams.put("created_at", today);
		xmessageparams.put("active", true);
		Number xmessageId =  insert.executeAndReturnKey(xmessageparams);   
		return xmessageId.intValue();
	
	}
	
	private ChannelDto findXMsgById(ChannelDto chanel){
		String groupsql = "SELECT msg.id as xmid, msg.sequence_num as sequenceNo, msg.httpmessagepath as httpmessagepath, xmsg.name as name, xmsg.id as xmsgid FROM xmessage xmsg, message msg where xmsg.id = msg.xmsg_id and msg.id=?";
		List<Map<String,Object>> channelmap = null;
		ChannelDto cdto = null;
		try{
			channelmap =  jdbcTemplate.queryForList(groupsql, new Object[]
	        { chanel.getxMsgId() });
			
			cdto = new ChannelDto();
			
			for(Map<String,Object> map:channelmap){
				cdto.setName(map.get("name").toString());
				cdto.setHttpmessagepath(map.get("httpmessagepath").toString());
				cdto.setSequenceNo(Integer.parseInt(map.get("sequenceNo").toString()));
				cdto.setxMsgId(Integer.parseInt(map.get("cid").toString()));
				cdto.setMessageId(Integer.parseInt(map.get("xmid").toString()));
			}
			
			}catch(EmptyResultDataAccessException e){
				return null;
			}
		
		return cdto;
	}
	
	public Integer getMaxMessageSequence(Integer xMsgId) {
		Integer seqNum = 0;
		String maxSeqSQL = "Select  max(sequence_num)  from  message where xmsg_id = ?";
		List<Map<String,Object>> resultmap = null;
		try{
			resultmap =  jdbcTemplate.queryForList(maxSeqSQL, new Object[]
	        {xMsgId});
			
			for(Map<String,Object> map:resultmap){
				seqNum = (Integer) map.get("sequence_num");
				
			}
			
		}catch(EmptyResultDataAccessException e){
				
		}
		return seqNum;
	}
	
	public Collection<GroupDto> findGroupDetailsByUserId(Integer userId){
		//String gdsql = "SELECT gps.ID as groupid, gps.name as groupName ,gps.owner as owner, apu.email as ownerEmail, gms.userid as memeberId, apu1.email as memberEmail FROM APIUser apu join  groups gps on apu.ID = gps.owner join groupsmembers gms on gps.ID=gms.groupid join APIUser apu1 on gms.userid = apu1.id where gps.owner=?";
		String gdsql = "SELECT gps.ID as groupid, gps.name as groupName ,gps.owner as owner, apu.email as ownerEmail, gms.userid as memeberId, apu1.email as memberEmail FROM groups gps left join  APIUser apu   on apu.ID = gps.owner left join groupsmembers gms on gps.ID=gms.groupid left join APIUser apu1 on gms.userid = apu1.id where gps.owner=?";
		
		List<Map<String,Object>> groupsmap = null;
		GroupDto gdto = null;
		List<Integer> ownerIds = null;
		Map<String,GroupDto> groupnames = new HashMap<String, GroupDto>();
		try{
			groupsmap =  jdbcTemplate.queryForList(gdsql, new Object[]
	        {userId});
			
			if(groupsmap == null || groupsmap.isEmpty()){
				ownerIds = findGroupOwnerByMembersId(userId);
				if(ownerIds == null || ownerIds.isEmpty()){
					return groupnames.values();
				}
				gdsql = "SELECT gps.ID as groupid, gps.name as groupName ,gps.owner as owner, apu.email as ownerEmail, gms.userid as memeberId, apu1.email as memberEmail FROM APIUser apu join  groups gps on apu.ID = gps.owner join groupsmembers gms on gps.ID=gms.groupid join APIUser apu1 on gms.userid = apu1.id where gps.owner in (";
				for(Integer Id:ownerIds){
					gdsql = gdsql+Id+",";
				}
				gdsql = gdsql.substring(0, gdsql.length()-1) + ")";
				groupsmap =  jdbcTemplate.queryForList(gdsql, new Object[]
				        {});
			}
			
			
			
			
			gdto = new GroupDto();
			gdto.setEmails(new ArrayList<String>());
			Integer grpId = null;
			String groupName=null;
			String ownerEmail=null;
			for(Map<String,Object> map:groupsmap){
				groupName = map.get("groupName").toString();
				
				
				if(groupnames.containsKey(groupName)){
					groupnames.get(groupName).getEmails().add(map.get("memberEmail").toString());
				}else{
					gdto = new GroupDto();
					gdto.setEmails(new ArrayList<String>());
					if(map.get("memberEmail") != null){
						gdto.getEmails().add(map.get("memberEmail").toString());
					}
					grpId = Integer.parseInt(map.get("groupid").toString());
					ownerEmail = map.get("ownerEmail").toString();
					gdto.setId(grpId);
					gdto.setName(groupName);
					gdto.setOwneremail(ownerEmail);
					groupnames.put(groupName, gdto);
				}
				
			}
			
			}catch(EmptyResultDataAccessException e){
				return null;
			}
		
		return groupnames.values();
	}
	
	private List<Integer> findGroupOwnerByMembersId(Integer memberid){
		String ownersql = "select gps.owner as ownerId from groupsmembers gms join groups gps on gms.groupid=gps.id and gms.userid = ?";
		List<Integer> ownerids =null; 
		List<Map<String,Object>> ownerIds = null;
		try{
			ownerIds = jdbcTemplate.queryForList(ownersql, new Object[]
		        {memberid});
			if(!ownerIds.isEmpty()){
				ownerids = new ArrayList<Integer>(); 
				
				for(Map<String,Object> map:ownerIds){
					ownerids.add(Integer.parseInt(map.get("ownerId").toString()));
				}
				
			}
			
		}catch(EmptyResultDataAccessException e){
			return ownerids;
		}
		return ownerids;
		
	}
}
