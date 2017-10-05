package com.edspread.mobileapp.utils;

import java.io.File;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil {
	public static String CONTEXT_PATH = null;

	/*@SuppressWarnings("rawtypes")
	public static Map getSession() {
		return ActionContext.getContext().getSession();
	}

	public static HttpSession getSessionAction() {
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context
				.get(StrutsStatics.HTTP_REQUEST);
		return request.getSession();
	}*/

	public static String getContextPath() {
		if (CONTEXT_PATH == null) {
			CONTEXT_PATH = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServletContext().getRealPath("/");
			// CONTEXT_PATH =
			// CONTEXT_PATH.substring(0,CONTEXT_PATH.indexOf("pages")-1);
		}
		return CONTEXT_PATH;

	}

	public static String getServerDeploymentPath() {
		File fl = new File(getContextPath());
		return fl.getParent();

	}

	/*public static ServletContext getServletContext() {
		return (ServletContext) ActionContext.getContext().get(
				StrutsStatics.SERVLET_CONTEXT);
	}

	public static void logout() {
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context
				.get(StrutsStatics.HTTP_REQUEST);
		request.getSession().invalidate();
	}*/

}
