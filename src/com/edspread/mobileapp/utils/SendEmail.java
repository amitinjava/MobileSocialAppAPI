package com.edspread.mobileapp.utils;
import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;
public class SendEmail {

	
	public void send(final String to, String registrationCode ){ 
	Properties props = new Properties();    
    props.put("mail.smtp.host", "smtp.gmail.com");    
    props.put("mail.smtp.socketFactory.port", "465");    
    props.put("mail.smtp.socketFactory.class",    
              "javax.net.ssl.SSLSocketFactory");    
    props.put("mail.smtp.auth", "true");    
    props.put("mail.smtp.port", "465");   
    final String  from = "amitinjava@gmail.com";
    final  String fromPswd = "xyzzz";
    //get Session   
    Session session = Session.getDefaultInstance(props,    
     new javax.mail.Authenticator() {    
     protected PasswordAuthentication getPasswordAuthentication() {    
     return new PasswordAuthentication(from ,fromPswd);  
     }    
    });    
    //compose message    
    try {    
     MimeMessage message = new MimeMessage(session);    
     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
     message.setSubject("test");    
     message.setText("hello");    
     //send message  
     Transport.send(message);    
     System.out.println("message sent successfully");    
    } catch (MessagingException e) {throw new RuntimeException(e);}    
       
}  }  
