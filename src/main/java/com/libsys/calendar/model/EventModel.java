package com.libsys.calendar.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

//Ref https://developers.google.com/calendar/v3/reference/events

public class EventModel implements Serializable {
	
	
	private Integer moduleType;
	private Integer moduleSubType;
	private Integer moduleId;
	//Fields req for google model
	private String eventId;
	
	private String summary;
	private String location;
	private String description;
	private Date startDatetime;
	private Date endDatetime;
	List<String> recurrance; 
	private List<String> attendees;
	private Map<Integer,Integer> reminderType; // Key : Const.REMINDER_TYPE , Value : Minutes.
	private String colorId;
	private Short status; 				// Const.STATUS
	private boolean isUpdated;			//readOnly -Libsys
	
	private Boolean anyoneCanAddSelf;
	private Boolean guestsCanInviteOthers;
	private Boolean guestsCanModify;
	private Boolean guestsCanSeeOtherGuests;
	private Boolean privateCopy;
	private Boolean locked; //read only , if true eventModel.isLocked()Whether this is a locked event copy where no changes can be made to the main event fields "summary", "description", "location", "start", "end" or "recurrence". The default is False. Read-Only.
	private String hangoutLink; //read only
	
	
	
	
	public Integer getModuleType() {
		return moduleType;
	}
	public void setModuleType(Integer moduleType) {
		this.moduleType = moduleType;
	}
	public Integer getModuleSubType() {
		return moduleSubType;
	}
	public void setModuleSubType(Integer moduleSubType) {
		this.moduleSubType = moduleSubType;
	}
	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
		this.setUpdated(true);
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	 
	public Date getStartDatetime() {
		return startDatetime;
	}
	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}
	public Date getEndDatetime() {
		return endDatetime;
	}
	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}
	public List<String> getRecurrance() {
		return recurrance;
	}
	public void setRecurrance(List<String> recurrance) {
		this.recurrance = recurrance;
	}
	public Boolean isAnyoneCanAddSelf() {
		return anyoneCanAddSelf;
	}
	public void setAnyoneCanAddSelf(Boolean anyoneCanAddSelf) {
		this.anyoneCanAddSelf = anyoneCanAddSelf;
	}
	public Boolean isGuestsCanInviteOthers() {
		return guestsCanInviteOthers;
	}
	public void setGuestsCanInviteOthers(Boolean guestsCanInviteOthers) {
		this.guestsCanInviteOthers = guestsCanInviteOthers;
	}
	public Boolean isGuestsCanModify() {
		return guestsCanModify;
	}
	public void setGuestsCanModify(Boolean guestsCanModify) {
		this.guestsCanModify = guestsCanModify;
	}
	public Boolean isGuestsCanSeeOtherGuests() {
		return guestsCanSeeOtherGuests;
	}
	public void setGuestsCanSeeOtherGuests(Boolean guestsCanSeeOtherGuests) {
		this.guestsCanSeeOtherGuests = guestsCanSeeOtherGuests;
	}
	public Boolean isPrivateCopy() {
		return privateCopy;
	}
	public void setPrivateCopy(Boolean privateCopy) {
		this.privateCopy = privateCopy;
	}
	public Boolean isLocked() {
		return locked;
	}
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	public List<String> getAttendees() {
		return attendees;
	}
	public void setAttendees(List<String> attendees) {
		this.attendees = attendees;
	}
	public Map<Integer, Integer> getReminderType() {
		return reminderType;
	}
	public void setReminderType(Map<Integer, Integer> reminderType) {
		this.reminderType = reminderType;
	}
	public String getColorId() {
		return colorId;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public boolean isUpdated() {
		return isUpdated;
	}
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	
	
	
}
