package com.edspread.mobileapp.utils;


import java.io.Serializable;

public class Email implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String   senderAddress; 
	private String fromName;
    private String[] recepientAddresses;
    private String   messageTxt;
    private String   subjectTxt;
    private String[]   filePath;
    private String[] ccRecepientAddresses;
    private String[] replyToAddresses;
    private String[] bccRecepientAddresses;

    /**
     * @return Returns the messageTxt.
     */
    public String getMessageTxt()
    {
        return this.messageTxt;
    }

    /**
     * @return Returns the recipientAddresses.
     */
    public String[] getRecepientAddresses()
    {
        return this.recepientAddresses;
    }

    /**
     * @return Returns the senderAddress.
     */
    public String getSenderAddress()
    {
        return this.senderAddress;
    }

    /**
     * @return Returns the subjectTxt.
     */
    public String getSubjectTxt()
    {
        return this.subjectTxt;
    }

    /**
     * @param pMessageTxt:
     *            The messageTxt to set.
     */
    public void setMessageTxt(String pMessageTxt)
    {
        this.messageTxt = pMessageTxt;
    }

    /**
     * @param pRecipientAddresses:
     *            The recipientAddresses to set.
     */
    public void setRecepientAddresses(String[] pRecipientAddresses)
    {
        this.recepientAddresses = pRecipientAddresses;
    }

    /**
     * @param pSenderAddress:
     *            The senderAddress to set.
     */
    public void setSenderAddress(String pSenderAddress)
    {
        this.senderAddress = pSenderAddress;
    }

    /**
     * @param pSubjectTxt:
     *            The subjectTxt to set.
     */
    public void setSubjectTxt(String pSubjectTxt)
    {
        this.subjectTxt = pSubjectTxt;
    }

    /**
     * @return Returns the attachment file path.
     */
    public String[] getmFilePath() {
		return filePath;
	}

    /**
     * @param mFilePath:
     *            The File path to set.
     */
	public void setmFilePath(String[] mFilePath) {
		this.filePath = mFilePath;
	}

	/**
     * @return Returns the Cc Recepient Addresses.
     */
	public String[] getmCcRecepientAddresses() {
		return ccRecepientAddresses;
	}

	/**
     * @param mCcRecepientAddresses:
     *            The Cc Recepient Addresses to set.
     */
	public void setmCcRecepientAddresses(String[] mCcRecepientAddresses) {
		this.ccRecepientAddresses = mCcRecepientAddresses;
	}

	public String[] getReplyToAddresses() {
		return replyToAddresses;
	}

	public void setReplyToAddresses(String[] mReplyToAddresses) {
		this.replyToAddresses = mReplyToAddresses;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String[] getmBccRecepientAddresses() {
		return bccRecepientAddresses;
	}

	public void setmBccRecepientAddresses(String[] mBccRecepientAddresses) {
		this.bccRecepientAddresses = mBccRecepientAddresses;
	}
	
	
	
}
