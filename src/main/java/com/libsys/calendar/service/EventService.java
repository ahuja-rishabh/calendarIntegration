package com.libsys.calendar.service;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Colors;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.ColorDefinition;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.libsys.calendar.model.EventModel;
import com.libsys.calendar.model.EventParam;
import com.libsys.calendar.model.Reply;
import com.libsys.calendar.util.Const;
import com.libsys.calendar.util.ConversionHelper;

@Service
public class EventService {

	@Autowired
	ConversionHelper conversionHelper;
	
	public Reply fetchEvents(String calendarId,Calendar service,EventParam eventParam) throws IOException {
		Reply reply=new Reply();
        com.google.api.services.calendar.Calendar.Events.List list = service.events().list(calendarId);
        
        if(eventParam.getMaxResultCount()!=null) {
        	list.setMaxResults(eventParam.getMaxResultCount());
        }
        if(eventParam.getFromDate()!=null) {
        	DateTime datetime= new DateTime(eventParam.getFromDate().getTime());
        	list.setTimeMin(datetime);
        }
        if(eventParam.getOrderBy()!=null) {        
        	list.setOrderBy(Const.FILTER_PARAMS.ORDER_BY.findById(eventParam.getOrderBy()).getValue()).setSingleEvents(true);;
        }
        Events events = list.execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event, start);
            }
        }
        List<EventModel> eventModels=conversionHelper.convertFromGoogleEventModels(items);
        reply.setData(eventModels);
        return reply;
	}
	
	public Reply addUpdateEvents(String calendarId,Calendar service,List<EventModel> eventModels) throws IOException{
		Event googleEvent;
		Reply reply=new Reply();
		for(EventModel eventModel:eventModels) {
			if(eventModel.getEventId()==null) {
				googleEvent = conversionHelper.convertToGoogleEventModel(eventModel,null);
				if(googleEvent==null) {
					reply.setFlag(Const.REPLY_FLAG.WARNING.getId());
					reply.addMessage("Invalid params");
					continue;
				}
				googleEvent = service.events().insert(calendarId, googleEvent).execute();
				eventModel.setEventId(googleEvent.getId());
			}else {
				try {
					googleEvent = service.events().get(calendarId, eventModel.getEventId()).execute();
				}catch(IOException ioException) {
					System.out.println("Could not find event with id "+eventModel.getEventId());
					reply.setFlag(Const.REPLY_FLAG.WARNING.getId());
					continue;
				}
				googleEvent = conversionHelper.convertToGoogleEventModel(eventModel, googleEvent);
				googleEvent = service.events().update(calendarId, eventModel.getEventId(),googleEvent).execute();
				eventModel.setEventId(googleEvent.getId());
			}
		}
		reply.setData(eventModels);
		return reply;
	}
	
	public Reply deleteEvents(String calendarId,Calendar service,List<EventModel> eventModels){
		Reply reply= new Reply();
		Iterator<EventModel> itr= eventModels.iterator();
		EventModel eventModel;
		while(itr.hasNext()) {
			eventModel=itr.next();
			if(eventModel.getEventId()==null) {
				reply.setFlag(Const.REPLY_FLAG.WARNING.getId());
				continue;
			}
			
			try {
				service.events().delete(calendarId, eventModel.getEventId()).execute();
			} catch (IOException e) {
				reply.setFlag(Const.REPLY_FLAG.WARNING.getId());
				continue;
			}
			eventModel.setUpdated(true);
		}
		
		reply.setData(eventModels);
		return reply;
	}
	
	
	public String findCalendarIdBySummary(String summary,Calendar service) throws IOException{
		com.google.api.services.calendar.model.Calendar calendar;
		String pageToken = null;
		String calendarId=null;
		do {
		  com.google.api.services.calendar.model.CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
		  List<CalendarListEntry> items = calendarList.getItems();

		  for (CalendarListEntry calendarListEntry : items) {
			  if(calendarListEntry.getSummary().equals(summary)) {
				  calendarId=calendarListEntry.getId();
				  break;
			  }
		  }
		  pageToken = calendarList.getNextPageToken();
		} while (pageToken != null && calendarId==null);
		
		if(calendarId!=null) {
		calendar = service.calendars().get(calendarId).execute();
		}else {
			calendar = new com.google.api.services.calendar.model.Calendar();
			calendar.setSummary(summary);
			calendar.setTimeZone(Const.TIME_ZONE.INDIA.getValue());
			com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();
			calendarId=createdCalendar.getId();
		}
		return calendarId;
	}
	
	public Set<String> getEventColors(Calendar service) throws IOException{
		com.google.api.services.calendar.model.Colors colors = service.colors().get().execute();
		for (Map.Entry<String, ColorDefinition> color : colors.getEvent().entrySet()) {
			  System.out.println("ColorId : " + color.getKey());
			  System.out.println("  Background: " + color.getValue().getBackground());
			  System.out.println("  Foreground: " + color.getValue().getForeground());
			}
		return colors.getEvent().keySet();
	}
	
	
}
