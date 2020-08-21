package com.libsys.calendar.handler.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.libsys.calendar.model.EventModel;
import com.libsys.calendar.model.EventParam;
import com.libsys.calendar.model.Reply;
import com.libsys.calendar.service.Authorization;
import com.libsys.calendar.service.EventService;
import com.libsys.calendar.util.Const;

@RestController("CalendarController")
@RequestMapping("/api/google/calendar")
public class GoogleCalendarController {

//	@Autowired
//	OAuth2AuthorizedClientService clientService;
	@Autowired Authorization authorizationService;
	@Autowired EventService eventService;
	private static final String APPLICATION_NAME = /*"Google Calendar API Java Quickstart"*/ "Calendar Integration Application" ;
    private static final String CALENDAR_SUMMARY="lsac";
    private static final List<String> SCOPES = new ArrayList(CalendarScopes.all());


	@GetMapping("/")
	public String publicAcessMethod() {
		return "helloWorld";
	}
	
	
	@PostMapping("fetch/events")
	public Reply getEventListForUser(@RequestParam String userId , @RequestBody EventParam eventParam) {
		Reply reply= new Reply();
		Calendar service;
		try {
			service = authorizationService.getCalendarService(APPLICATION_NAME,userId,SCOPES);
			String calendarId = eventService.findCalendarIdBySummary(CALENDAR_SUMMARY, service);
			reply = eventService.fetchEvents(calendarId, service, eventParam);
		} catch (IOException | GeneralSecurityException e) {
			reply.setFlag(Const.REPLY_FLAG.ERROR.getId());
			reply.addMessage("Could not process request");
			e.printStackTrace();
		}
		return reply;
	}

	
	 @PostMapping("add/events")
	 public Reply addEvent(@RequestParam String userId ,@RequestBody List<EventModel>  eventModels) {
		 Reply reply=new Reply();
			try {
				Calendar service= authorizationService.getCalendarService(APPLICATION_NAME,userId,SCOPES);
				String calendarId = eventService.findCalendarIdBySummary(CALENDAR_SUMMARY, service);
				reply=eventService.addUpdateEvents(calendarId, service, eventModels);
				
			} catch (IOException | GeneralSecurityException e1) {
				reply.setFlag(Const.REPLY_FLAG.ERROR.getId());
				reply.addMessage("Could not process request");
				e1.printStackTrace();
			}
		 return reply;
	 }
	 
	 	
	 @PostMapping("update/events")
	 public Reply updateEvent(@RequestParam String userId,@RequestBody List<EventModel> eventModels) {
		 Reply reply=new Reply();
			try {
				Calendar service= authorizationService.getCalendarService(APPLICATION_NAME,userId,SCOPES);
				String calendarId = eventService.findCalendarIdBySummary(CALENDAR_SUMMARY, service);
				reply=eventService.addUpdateEvents(calendarId, service, eventModels);
				
			} catch (IOException | GeneralSecurityException e1) {
				reply.setFlag(Const.REPLY_FLAG.ERROR.getId());
				reply.addMessage("Could not process request");
				e1.printStackTrace();
			}
		 return reply;
	 }
	 
	 @PostMapping("delete/events")
	 public Reply deleteEventsFromCalendar(@RequestParam String userId,@RequestBody List<EventModel> eventModels)
	 {
		 Reply reply=new Reply();
		try {
			Calendar service = authorizationService.getCalendarService(APPLICATION_NAME,userId,SCOPES);
			String  calendarId = eventService.findCalendarIdBySummary(CALENDAR_SUMMARY, service);
			reply= eventService.deleteEvents(calendarId, service, eventModels);
		} catch (IOException | GeneralSecurityException e) {
			reply.setFlag(Const.REPLY_FLAG.ERROR.getId());
			reply.addMessage("Could not process request");
			e.printStackTrace();
		}
		return reply;
	 }
	
	 @GetMapping("fetch/colors")
	 public Set<String> getColorList() throws IOException, GeneralSecurityException{
		 String emailId="rishabhahuja2013";
		 Calendar service = authorizationService.getCalendarService(APPLICATION_NAME,emailId,SCOPES);
		 return eventService.getEventColors(service);
	 }
}
