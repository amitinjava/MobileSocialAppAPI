package com.edspread.mobileapp.utils;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;

import com.edspread.mobileapp.constants.MobileAppConstant;

public class AppUtillty {
	private EmailSettings settings;

	
	public String getValidationCode(int length) {
		Character strCharacters[] = {'1', '2', '3', '4', '5', '6',
				'7', '8', '9', '0'};
		int pos = 0;
		Random rGen = new Random();
		StringBuilder validationCode = new StringBuilder("");
		for (int x = 0; x < length; x++) {
			pos = rGen.nextInt(10);
			validationCode.append(strCharacters[pos]);
		}
		return validationCode.toString();
	}
	
	
	public String truncateContact(String contact) {
		if(contact.length()>MobileAppConstant.PHONENUMLENGTH){
			contact = contact.replaceAll("[^0-9]", "");
			return StringUtils.right(contact, MobileAppConstant.PHONENUMLENGTH);
		}else if(contact.length() == MobileAppConstant.PHONENUMLENGTH){
			return contact;
		}
		return null;
	}
	public List<String> truncateContactList(List<String> contactList) {
		for (String contact : contactList) {
			contact = truncateContact(contact);
		}
		return contactList;
	}
	
	public  boolean sendMail2Users(List<String> to, String text,
			String fromUser, String senderName, String subject, List<File> files) {
		if(!settings.getMode().equals("live")){
			return true;
		}
		Properties props = new Properties();
		//props.put("mail.smtp.user", settings.getUsername());
		props.put("mail.smtp.host", settings.getHost());
		if (!"".equals(settings.getPort()))
			props.put("mail.smtp.port", settings.getPort());
		if (!"".equals("true"))
			props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		if (settings.isDebug()) {
			props.put("mail.smtp.debug", "true");
		}else{
			props.put("mail.smtp.debug", "false");
		}
		if (!"".equals(settings.getPort()))
			props.put("mail.smtp.socketFactory.port", settings.getPort());
		if (!"".equals(settings.getSocketFactoryClass()))
			props.put("mail.smtp.socketFactory.class",
					settings.getSocketFactoryClass());
		if (!"".equals("false"))
			props.put("mail.smtp.socketFactory.fallback", "false");
		try {
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(true);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromUser,"Text Time Machine"));
			msg.setSubject(subject);
			msg.setContent(text, "text/html");
			for (int i = 0; i < to.size(); i++) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						to.get(i)));
			}

			msg.saveChanges();

			Transport transport = session.getTransport("smtp");
			transport.connect(settings.getHost(), settings.getUsername(),
					settings.getPassword());
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception mex) {
			mex.printStackTrace();
			return false;
		}

	}

	public EmailSettings getSettings() {
		return settings;
	}

	public void setSettings(EmailSettings settings) {
		this.settings = settings;
	}

}
