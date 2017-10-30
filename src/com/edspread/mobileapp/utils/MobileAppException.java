package com.edspread.mobileapp.utils;


public class MobileAppException extends java.lang.Exception {

	private static final long serialVersionUID = 1L;
	
	public MobileAppException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public MobileAppException(Throwable cause)
	{
		super(cause);
	}
	
	public MobileAppException(String message)
	{
		super(message);
	}
}
