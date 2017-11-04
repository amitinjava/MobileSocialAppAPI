package com.edspread.mobileapp.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.edspread.mobileapp.dto.GroupDto;
import com.edspread.mobileapp.entity.User;

public class GroupDao {
	private JdbcTemplate jdbcTemplate;  
	  
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
	    this.jdbcTemplate = jdbcTemplate;  
	}  
	
	public Integer saveGroupMembers(GroupDto gdto) throws DuplicateKeyException{
		
		Date today = new Date(); 
		Number groupId = saveGroup(gdto, today);
		
		
		return Integer.parseInt(groupId.toString());
	}
	
	private Number saveGroup(GroupDto gdto, Date today) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("groups")
				.usingGeneratedKeyColumns("id");
		;

		Map<String, Object> groupparams = new HashMap();
		groupparams.put("ID", gdto.getId());
		groupparams.put("name", gdto.getName());
		groupparams.put("owner", gdto.getName());
		groupparams.put("created_at", today);
		groupparams.put("updated_at", today);
		groupparams.put("active", true);
		final Number groupId = insert.executeAndReturnKey(groupparams);
		List<User> users = getMembersId(gdto.getEmails());
		
		
		String sql = "insert into GroupsMembers values(?,?,?,?,?,?,?,?)";
		for(User user:users){
		System.out.println(groupId); 
		       int status = jdbcTemplate.update(sql, new Object[]
		        {null, groupId, user.getId(), today,today,null,null, true});
		}
		return groupId;
	}
	
	public List<User> getMembersId(List<String> emails){
		String sql = "select * from APIUser where email in (";
		for(String email : emails){
			sql = sql+"'"+email+"'";
			if(emails.size() > 1){
				sql =sql+",";
			}
		}
		sql = sql.substring(0, sql.length()-1);
		sql = sql +") and active=1";
		System.out.println(sql);
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

	public GroupDto getGroupMembers(Integer groupId) {
		String groupsql = "SELECT gs.id as groupid, au.email as email, gs.name as name FROM apiuser au, groups gs, groupsmembers gms where au.ID = gms.userid and gs.id = gms.groupid and gs.id=?";
		List<Map<String,Object>> groupsmap = null;
		GroupDto gdto = null;
		try{
			groupsmap =  jdbcTemplate.queryForList(groupsql, new Object[]
	        { groupId });
			
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

}
