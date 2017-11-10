package com.edspread.mobileapp.dto;

public class ChannelDto {
	private String name;
	private String owneremail;
	private Integer xMsgId;
	private Integer channelId;
	private String httpmessagepath;
	private Integer sequenceNo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwneremail() {
		return owneremail;
	}
	public void setOwneremail(String owneremail) {
		this.owneremail = owneremail;
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
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	

}
