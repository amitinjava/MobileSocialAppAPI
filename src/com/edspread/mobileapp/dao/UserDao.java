package com.edspread.mobileapp.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import com.edspread.mobileapp.dto.UserDto;
import com.edspread.mobileapp.entity.User;

public class UserDao {
	
	private JdbcTemplate jdbcTemplate;  
	  
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
	    this.jdbcTemplate = jdbcTemplate;  
	}  
	public int register(UserDto user){
		
		
		String sql = "insert into APIUser values(?,?,?,?,?)";

       int status = jdbcTemplate.update(sql, new Object[]
        { user.getId(), user.getEmail(), user.getPassword(), user.getRegistrationCode(), false});
		
		return status;
	}
	
	public Integer login(String email, String password){
		String sql = "select * from APIUser where email=? and password = ?";
		User usr = null;
		try{
		usr = (User) jdbcTemplate.queryForObject(sql, new Object[]
        { email, password }, new RowMapper()
        {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException
            {
            	
            	User usr = new User();
            	usr.setId(rs.getInt(1));
            	usr.setEmail(rs.getString(2));
                return usr;
            }
        });
		
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		
		
        return usr.id;
	}
	
	public Integer activate(UserDto user){
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
                return usr;
            }
        });
		
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		if(usr == null){
			return null;
		}
		System.out.println(usr.id);
		if(usr != null){
			String usql = "update APIUser set active =? where id=?";
	        jdbcTemplate.update(usql, new Object[]
	        { true, usr.id });
	    }
        return usr.id;
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
		}
        return udo;
	}

}
