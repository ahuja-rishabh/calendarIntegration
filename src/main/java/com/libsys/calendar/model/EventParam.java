package com.libsys.calendar.model;

import java.util.Calendar;
import java.util.Date;

public class EventParam {

	private Date fromDate;
	private Integer maxResultCount;
	private Short orderBy; 
	
	
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Integer getMaxResultCount() {
		return maxResultCount;
	}
	public void setMaxResultCount(Integer maxResultCount) {
		this.maxResultCount = maxResultCount;
	}
	public Short getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Short orderBy) {
		this.orderBy = orderBy;
	}
	
	
	
}
