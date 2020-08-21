package com.libsys.calendar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.libsys.calendar.util.Const;

public class Reply {
	
	private Object data;
	
	private Map<String, Object> infoMap;
	
	private Short flag;
	
	private List<String> messageList;

	public Reply() {
	    flag=Const.REPLY_FLAG.SUCCESS.getId();
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Map<String, Object> getInfoMap() {
		return infoMap;
	}

	public void setInfoMap(Map<String, Object> infoMap) {
		this.infoMap = infoMap;
	}

	public Short getFlag() {
		return flag;
	}

	public void setFlag(Short flag) {
		this.flag = flag;
	}
	
	public void addMessage(String e) {
		this.messageList = this.messageList==null ? new ArrayList<>():this.messageList ;
		messageList.add(e);
	}
	
}
