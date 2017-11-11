package com.edspread.mobileapp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.edspread.mobileapp.dto.UserDto;
import com.edspread.mobileapp.entity.User;
import com.edspread.mobileapp.entity.UserDetail;
import com.edspread.mobileapp.utils.PasswordGenerator;

public class UserDao {
	
	private JdbcTemplate jdbcTemplate;  
	  
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
	    this.jdbcTemplate = jdbcTemplate;  
	}  
	public int register(UserDto user) throws DuplicateKeyException{
		
		
		String sql = "insert into APIUser values(?,?,?,?,?)";

       int status = jdbcTemplate.update(sql, new Object[]
        { user.getId(), user.getEmail(), user.getPassword(), user.getRegistrationCode(), false});
		
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
	
	
	public User getDeactivateUser(UserDto user){
		String sql = "select * from APIUser where email=? and active=0";
		User usr = null;
		try{
		usr = (User) jdbcTemplate.queryForObject(sql, new Object[]
        { user.getEmail() }, new RowMapper()
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
		return usr;
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
	
	public UserDto getUserDetails(String email){
		UserDto udo = null;
		return udo;
	}
	
	public int addUserDetails(UserDto udo){
		User usr = findUserByEmail(udo.getEmail());
		int status = 0;
		if(usr != null){
			Date today = new Date();
			UserDetail udetail=getUserDetailsByEmail(udo.getEmail());
			if(udetail == null){
			String sql = "insert into userdetails values(?,?,?,?,?,?,?,?,?,?)";

	       status = jdbcTemplate.update(sql, new Object[]
	        { null, usr.getId(), udo.getName(), udo.getMobile(),udo.getProfilePix(),today,today,null,null, true});
			}else{
				String updatesql = "update userdetails set name =? , mobile=?, profilepix=?, updated_at=? where id=?";

			       status = jdbcTemplate.update(updatesql, new Object[]
			        { udo.getName(), udo.getMobile(),udo.getProfilePix(),today,udetail.getId()});
			}
		}
			return status;
		
	}
	
	public UserDetail getUserDetailsByEmail(String email){
		User usr = findUserByEmail(email);
		int status = 0;
		UserDetail ud = null;
		if(usr != null){
			String sql = "select * from userdetails where userid=?";
			
			try{
				ud = (UserDetail) jdbcTemplate.queryForObject(sql, new Object[]
		        { usr.getId() }, new RowMapper()
		        {
		            @Override
		            public UserDetail mapRow(ResultSet rs, int rowNum) throws SQLException
		            {
		            	
		            	UserDetail usr = new UserDetail();
		            	usr.setId(rs.getInt(1));
		            	usr.setUserId(rs.getInt(2));
		            	usr.setName(rs.getString(3));
		            	usr.setMobile(Integer.parseInt(rs.getString(4)));
		            	usr.setProfilePix(rs.getString(5));
		                return usr;
		            }
		        });
				
				}catch(EmptyResultDataAccessException e){
					return null;
				}
		}
			return ud;
		
	}
	
	
	
	public User findUserByEmail(String email){
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
		}catch (Exception e) {
			e.printStackTrace();
		}
		return usr;
	}
	
	public User findUserById(Integer id){
		String sql = "select * from APIUser where id=?";
		User usr = null;
		try{
		usr = (User) jdbcTemplate.queryForObject(sql, new Object[]
        { id }, new RowMapper()
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
		}catch (Exception e) {
			e.printStackTrace();
		}
		return usr;
	}

}
