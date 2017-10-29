package com.edspread.mobileapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.edspread.mobileapp.dto.XmppUserDTO;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class JDBCUtill {

	
private static Connection getConnection() {
		
		Connection con = null;
		String userName = "root";
		String password = "123456";
	    String url = "jdbc:mysql://localhost:3306/txttimemac";
	 	try {
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		 
	    con = DriverManager.getConnection (url, userName, password);
	    System.out.println ("Database connection established");
	    } catch (InstantiationException e) {
			
			e.printStackTrace();
		}
	 	 catch (IllegalAccessException e) {
				
				e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	    return con;
	}

public static int addXMPPUser(XmppUserDTO xmppUserDTO) throws Exception  {
	 try {
			Connection con = getConnection();
			PreparedStatement ps;
		    ps = con.prepareStatement("insert into ofuser " +
		            		"(username,plainPassword,encryptedPassword,name,email,creationDate,modificationDate) " +
		            		"values" +
		            		"(?,?,?,?,?,?,?)");
		    ps.setString(1, xmppUserDTO.getUserName());
		    ps.setString(2, xmppUserDTO.getPlainPassword());
		    ps.setString(3, xmppUserDTO.getEncryptedPassword());
		    ps.setString(4, xmppUserDTO.getName());
		    ps.setString(5, xmppUserDTO.getEmail());
		    ps.setString(6, xmppUserDTO.getCreatedAt());
		    ps.setString(7, xmppUserDTO.getUpdatedAt());
		    
		    return ps.executeUpdate();
		 }catch(MySQLIntegrityConstraintViolationException s){
			
			 return -1;
		 }catch(SQLException s){
			 s.printStackTrace();
			 throw s; 
		 }
		 catch (Exception e) {
			e.printStackTrace();
			throw e;
		 }
	
	}
}
