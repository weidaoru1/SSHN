package com.fus.cocoon.spring;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fus.cocoon.utils.object.ReflectionUtils;
public final class StringUtilsExt {
	public static String lowerCasePrefixString(String str){
		if (StringUtils.isNotBlank(str)) {
			str = str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
		}
		return str;
	}
	public static String upperCasePrefixString(String str){
		if (StringUtils.isNotBlank(str)) {
		    str = str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
		}
		return str;
	}
	public static String findWordBeforeFirstUppercaseCharacter(String str)
	  {
	    if (StringUtils.isNotBlank(str)) {
	      for (int i = 0; i < str.length(); i++) {
	        if (Character.isUpperCase(str.charAt(i))) {
	          i = i == 0 ? 1 : i;
	          return str.substring(0, i);
	        }
	      }
	    }

	    return "";
	  }
	public static int getLength(String str)
	  {
	    if (StringUtils.isNotBlank(str)) {
	      try {
	        return new String(str.getBytes("GB2312"), "ISO-8859-1").length();
	      }
	      catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	      }
	    }

	    return 0;
	  }
	public static String fillStringAfter(String str, String filler, int fillNum)
	  {
	    if ((str != null) && (filler != null) && (fillNum > 0)) {
	      int strLength = getLength(str);
	      if (strLength < fillNum) {
	        fillNum -= strLength;
	        for (int i = 0; i < fillNum; i++) {
	          str = str + filler;
	        }
	      }
	    }
	    return str;
	  }

	  public static void trimObject(Object obj)
	  {
	    List<Field> fieldList = ReflectionUtils.getDeclaredFieldsNoneTransient(obj);
	    if ((fieldList != null) && (fieldList.size() > 0))
	      try {
	        for (Field field : fieldList) {
	          if (String.class.equals(field.getType())) {
	            field.setAccessible(true);
	            String value = (String)field.get(obj);
	            if (value != null)
	              field.set(obj, value.trim());
	          }
	        }
	      }
	      catch (IllegalArgumentException e)
	      {
	        e.printStackTrace();
	      }
	      catch (IllegalAccessException e) {
	        e.printStackTrace();
	      }
	  }
}
