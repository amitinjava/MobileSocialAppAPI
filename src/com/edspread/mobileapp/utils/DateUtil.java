package com.edspread.mobileapp.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.edspread.mobileapp.constants.MobileAppConstant;

public class DateUtil {
	
	public static Date getCurrentDateTime(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.getTime();
	}
	public static long getTimeInLong(){
		return System.currentTimeMillis();
	}
	public static String getFormattedDateTime (){
		Date dt = DateUtil.getTodayDate();
		SimpleDateFormat formatter = new SimpleDateFormat(MobileAppConstant.DATE_FORMAT);
		return formatter.format(dt);
	}
	public static Date getTodayDate(){
		return new Date(new GregorianCalendar().getTimeInMillis());
	}
}

