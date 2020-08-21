package com.libsys.calendar.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.libsys.calendar.model.EventModel;

@Component
public class ConversionHelper {

	public List<Event> convertToGoogleEventModels(List<EventModel> eventModelList) {
		Iterator<EventModel> iterator = eventModelList.iterator();
		List<Event> eventModels = new ArrayList<>();
		Event event;
		while (iterator.hasNext()) {
			event = convertToGoogleEventModel(iterator.next(), null);
			eventModels.add(event);
		}
		return eventModels;
	}

	/**
	 * 
	 * To create a valid event model send googleEvent null. To create a patch send
	 * the required fields.
	 * 
	 * @param eventModel
	 * @param googleEvent
	 * @return <b> null </b> when invalid parameters for creation <b> otherwise
	 *         </b>updated Event model.
	 */
	public Event convertToGoogleEventModel(EventModel eventModel, Event googleEvent) {

		if (googleEvent == null && (eventModel.getSummary() == null || eventModel.getDescription() == null
				|| eventModel.getStartDatetime() == null
				|| eventModel.getEndDatetime() == null)) {
			return null;
		}
		googleEvent = googleEvent == null ? new Event() : googleEvent;

		googleEvent.setSummary(eventModel.getSummary());
		googleEvent.setLocation(eventModel.getLocation());
		googleEvent.setDescription(eventModel.getDescription());

		;
		DateTime startDateTime = new DateTime(eventModel.getStartDatetime().getTime());
		EventDateTime start = new EventDateTime().setDateTime(startDateTime)
				.setTimeZone(Const.TIME_ZONE.INDIA.getValue());
		googleEvent.setStart(start);

		DateTime endDateTime = new DateTime(eventModel.getEndDatetime().getTime());
		System.out.println("******END DATE TIME******\n" + endDateTime);
		EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone(Const.TIME_ZONE.INDIA.getValue());
		googleEvent.setEnd(end);

		if (eventModel.getRecurrance() != null && !eventModel.getRecurrance().isEmpty()) {
			googleEvent.setRecurrence(eventModel.getRecurrance());
		}

		if (eventModel.getAttendees() != null && !eventModel.getAttendees().isEmpty()) {
			List<EventAttendee> eventAttendees = new ArrayList<>();
			eventModel.getAttendees().forEach(email -> eventAttendees.add(new EventAttendee().setEmail(email)));
			googleEvent.setAttendees(eventAttendees);
		}
		if (eventModel.getReminderType() != null && !eventModel.getReminderType().isEmpty()) {
			List<EventReminder> reminderOverrides = new ArrayList();
			eventModel.getReminderType().forEach((type, minutes) -> reminderOverrides.add(
					new EventReminder().setMethod(Const.REMINDER_TYPE.findById(type).getValue()).setMinutes(minutes)));
			Event.Reminders reminders = new Event.Reminders().setUseDefault(false).setOverrides(reminderOverrides);
			googleEvent.setReminders(reminders);
		}

		if (eventModel.getColorId() != null) {
			googleEvent.setColorId(eventModel.getColorId());
		}

		if (eventModel.getStatus() != null) {
			googleEvent.setStatus(Const.STATUS.findById(eventModel.getStatus()).getValue());
		}

		if (eventModel.isAnyoneCanAddSelf() != null) {
			googleEvent.setAnyoneCanAddSelf(eventModel.isAnyoneCanAddSelf());
		}

		if (eventModel.isGuestsCanInviteOthers() != null) {
			googleEvent.setGuestsCanInviteOthers(eventModel.isGuestsCanInviteOthers());
		}

		if (eventModel.isGuestsCanSeeOtherGuests() != null) {
			googleEvent.setGuestsCanSeeOtherGuests(eventModel.isGuestsCanSeeOtherGuests());
		}

		if(eventModel.isGuestsCanModify()!=null) {
			googleEvent.setGuestsCanModify(eventModel.isGuestsCanModify());
		}
		if (eventModel.isPrivateCopy() != null) {
			googleEvent.setPrivateCopy(eventModel.isPrivateCopy());
		}

		return googleEvent;
	}

	public List<EventModel> convertFromGoogleEventModels(List<Event> googleEventModels) {
		List<EventModel> eventModels = new ArrayList<>();
		Iterator<Event> itr = googleEventModels.iterator();
		Event googleEventModel;
		EventModel eventModel;

		while (itr.hasNext()) {
			googleEventModel = itr.next();
			eventModel = new EventModel();
			eventModel.setEventId(googleEventModel.getId());
			eventModel.setSummary(googleEventModel.getSummary());
			eventModel.setLocation(googleEventModel.getLocation());
			eventModel.setDescription(googleEventModel.getDescription());
			eventModel.setStartDatetime(new Date(googleEventModel.getStart().getDateTime().getValue()));
			eventModel.setEndDatetime(new Date(googleEventModel.getEnd().getDateTime().getValue()));

			if (googleEventModel.getRecurrence() != null && !googleEventModel.getRecurrence().isEmpty()) {
				eventModel.setRecurrance(googleEventModel.getRecurrence());
			}

			if (googleEventModel.getAttendees() != null && !googleEventModel.getAttendees().isEmpty()) {
				List<String> eventAttendees = new ArrayList<>();
				googleEventModel.getAttendees().forEach(email -> eventAttendees.add(email.getEmail()));
				eventModel.setAttendees(eventAttendees);
			}
			if (googleEventModel.getReminders() != null && !googleEventModel.getReminders().isEmpty()
					&& googleEventModel.getReminders().getOverrides() != null) {
				Map<Integer, Integer> reminderType = new HashMap();
				googleEventModel.getReminders().getOverrides().forEach(e -> {
					reminderType.put(Const.REMINDER_TYPE.findByValue(e.getMethod()).getId(), e.getMinutes());
				});
				eventModel.setReminderType(reminderType);
			}

			if (googleEventModel.getColorId() != null) {
				eventModel.setColorId(googleEventModel.getColorId());
			}

			if (googleEventModel.getStatus() != null) {
				eventModel.setStatus((short) Const.STATUS.findByValue(googleEventModel.getStatus()).getId());
			}
			eventModel.setAnyoneCanAddSelf(googleEventModel.isAnyoneCanAddSelf());
			eventModel.setGuestsCanInviteOthers(googleEventModel.isGuestsCanInviteOthers());
			eventModel.setGuestsCanSeeOtherGuests(googleEventModel.isGuestsCanSeeOtherGuests());
			eventModel.setGuestsCanModify(googleEventModel.isGuestsCanModify());
			eventModel.setPrivateCopy(googleEventModel.isPrivateCopy());
			eventModels.add(eventModel);
		}
		return eventModels;

	}
}
