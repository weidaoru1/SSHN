package com.fus.cocoon.dict;

public enum RecordState {
	NORMAL, 
	  DELETED;
	public String state()
	  {
	    return String.valueOf(ordinal());
	  }
}
