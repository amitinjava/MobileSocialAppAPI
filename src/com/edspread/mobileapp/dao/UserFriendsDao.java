package com.edspread.mobileapp.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import com.edspread.mobileapp.dto.UserDto;
import com.edspread.mobileapp.entity.User;
import com.edspread.mobileapp.entity.UserFriends;
import com.edspread.mobileapp.utils.PasswordGenerator;

public class UserFriendsDao {
	
	private JdbcTemplate jdbcTemplate;  
	  
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
	    this.jdbcTemplate = jdbcTemplate;  
	}  
	public int saveFriend(UserFriends ufrnd) throws DuplicateKeyException{
		
		
		String sql = "insert into UserFriends values(?,?,?)";

       int status = jdbcTemplate.update(sql, new Object[]
        { ufrnd.getId(), ufrnd.getUserId(), ufrnd.getFriendId()});
		
		return status;
	}
	
	public UserDto login(String email, String password){
		String sql = "select * from APIUser where email=?";
		User usr = null;
		try{
		usr = (User) jdbcTemplate.queryForObject(sql, new Object[]
        { email }, new RowMapper()
        {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException
            {
            	
            	User usr = new User();
            	usr.setId(rs.getInt(1));
            	usr.setEmail(rs.getString(2));
            	usr.setPassword(rs.getString(3));
            	usr.setActive(rs.getBoolean(5));
                return usr;
            }
        });
		
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		String dpassword = PasswordGenerator.decryptPassword(usr.password);
		UserDto udo = new UserDto();
		if( usr.id != null  && password.equals(dpassword)){
			udo.active = usr.active;
			return udo;
		}else{
			return null;
		}
        
	}
	
	
	public List<User> getFriends(String email){
		String sql = "select * from APIUser where email !=? and active=1";
		List<Map<String,Object>> usrs = null;
		List<User> userList = new ArrayList<User>();
		try{
		usrs =  jdbcTemplate.queryForList(sql, new Object[]
        { email });
		
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
	
	public int updateRegistrationCode(String email, String registrationCode, String password){
			String usql = "update APIUser set registrationCode =? , password=? where email=?";
	        int status = jdbcTemplate.update(usql, new Object[]
	        { registrationCode, password, email });
	    return status;
	}
	
	public User activate(UserDto user){
		String sql = "select * from APIUser where email=? and registrationCode=?";
		User usr = null;
		try{
		usr = (User) jdbcTemplate.queryForObject(sql, new Object[]
        { user.getEmail(), user.getRegistrationCode() }, new RowMapper()
        {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException
            {
            	
            	User usr = new User();
            	usr.setId(rs.getInt(1));
            	usr.setEmail(rs.getString(2));
            	usr.setPassword(rs.getString(3));
                return usr;
            }
        });
		
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		if(usr == null){
			return null;
		}
		if(usr != null){
			String usql = "update APIUser set active =? where id=?";
	        jdbcTemplate.update(usql, new Object[]
	        { true, usr.id });
	    }
        return usr;
	}
	
	public UserDto forgotPassword(String email){

		String sql = "select * from APIUser where email=?";
		User usr = null;
		try{
		usr = (User) jdbcTemplate.queryForObject(sql, new Object[]
        { email }, new RowMapper()
        {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException
            {
            	
            	User usr = new User();
            	usr.setId(rs.getInt(1));
            	usr.setEmail(rs.getString(2));
            	usr.setPassword(rs.getString(3));
            	usr.setActive(rs.getBoolean(5));
                return usr;
            }
        });
		
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		UserDto udo = null;
		if(usr.id != null){
			udo = new UserDto();
			udo.password = usr.password;
			udo.active = usr.active;
		}
        return udo;
	}

}
