package com.edspread.mobileapp.dto;

import java.util.List;

public class ChannelDto {
	private String name;
	private Integer from;
	private Integer xMsgId;
	private Integer messageId;
	private String httpmessagepath;
	private Integer sequenceNo;
	private List<Integer> toList;
	private String title;
	private Integer userId;
	private String httpaPath;
	
	public Integer getMessageId() {
		return messageId;
	}
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}
	public List<Integer> getToList() {
		return toList;
	}
	public void setToList(List<Integer> toList) {
		this.toList = toList;
	}
	public String getHttpmessagepath() {
		return httpmessagepath;
	}
	public void setHttpmessagepath(String httpmessagepath) {
		this.httpmessagepath = httpmessagepath;
	}
	public Integer getxMsgId() {
		return xMsgId;
	}
	public void setxMsgId(Integer xMsgId) {
		this.xMsgId = xMsgId;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getHttpaPath() {
		return httpaPath;
	}
	public void setHttpaPath(String httpaPath) {
		this.httpaPath = httpaPath;
	}
	
	

}
