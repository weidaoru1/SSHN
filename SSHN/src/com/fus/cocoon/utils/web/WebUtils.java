package com.fus.cocoon.utils.web;

import java.net.URLDecoder;

public final class WebUtils {
	public static String decodeURL(Object target, String charset){
		String result = target != null ? target.toString() : "";
		try {
			result = URLDecoder.decode(result, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return result;
	}
}
