package com.edspread.mobileapp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.edspread.mobileapp.dto.GroupDto;
import com.edspread.mobileapp.entity.Groups;
import com.edspread.mobileapp.entity.User;

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
		System.out.println(groupId); 
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
	
	
}
