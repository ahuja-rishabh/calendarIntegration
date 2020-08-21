package com.libsys.calendar.util;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;

public class Const {

	public static final String TOKENS_DIRECTORY_PATH = "user_token";
	public static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	public static final List<String> SCOPES = new ArrayList(CalendarScopes.all());
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	static enum RECURRING_FREQUENCY {
		DAILY(1, "DAILY"), WEEKLY(2, "WEEKLY"), MONTHLY(3, "MONTHLY");
		int id;
		String value;

		private RECURRING_FREQUENCY(int id, String value) {
			this.id = id;
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public String getValue() {
			return value;
		}

		public static RECURRING_FREQUENCY findById(int id) {
			for (RECURRING_FREQUENCY entry : RECURRING_FREQUENCY.values()) {
				if (entry.getId() == id) {
					return entry;
				}
			}
			return null;
		}
	}

	static enum REMINDER_TYPE {
		UNDEFINED (0,"Undefined"),MAIL(1, "email"), POPUP(2, "popup");
		int id;
		String value;

		private REMINDER_TYPE(int id, String value) {
			this.id = id;
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public String getValue() {
			return value;
		}

		public static REMINDER_TYPE findById(int id) {
			for (REMINDER_TYPE entry : REMINDER_TYPE.values()) {
				if (entry.getId() == id) {
					return entry;
				}
			}
			return UNDEFINED;
		}
		public static REMINDER_TYPE findByValue(String value) {
			for (REMINDER_TYPE entry : REMINDER_TYPE.values()) {
				if (entry.getValue().equals(value)) {
					return entry;
				}
			}
			return UNDEFINED;
		}
		
	}

	public static enum TIME_ZONE {
		INDIA(1, "Asia/Kolkata");
		int id;
		String value;

		private TIME_ZONE(int id, String value) {
			this.id = id;
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public String getValue() {
			return value;
		}

		public static TIME_ZONE findById(int id) {
			for (TIME_ZONE entry : TIME_ZONE.values()) {
				if (entry.getId() == id) {
					return entry;
				}
			}
			return null;
		}

	}

	public static enum STATUS {
		UNDEFINED (0,"Undefined"),CONFIRMED(1, "confirmed"), TENTATIVE(2, "tentative"), CANCELLED(3, "cancelled");
		private int id;
		private String value;

		private STATUS(int id, String value) {
			this.id = id;
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public String getValue() {
			return value;
		}

		public static STATUS findById(int id) {
			for (STATUS entry : STATUS.values()) {
				if (entry.getId() == id) {
					return entry;
				}
			}
			return UNDEFINED;
		}
		public static STATUS findByValue(String value) {
			for (STATUS entry : STATUS.values()) {
				if (entry.getValue().equals(value)) {
					return entry;
				}
			}
			return UNDEFINED;
		}
	}

	public static enum REPLY_FLAG {
		ERROR((short) 1), WARNING((short) 2), SUCCESS((short) 3);
		short id;

		private REPLY_FLAG(short id) {
			this.id = id;
		}

		public short getId() {
			return id;
		}
	}

	public interface FILTER_PARAMS {
		public static enum ORDER_BY {
			START_TIME(1, "startTime"), UPDATED(2, "updated");
			private int id;
			private String value;

			private ORDER_BY(int id, String value) {
				this.id = id;
				this.value = value;
			}

			public int getId() {
				return id;
			}

			public String getValue() {
				return value;
			}

			public static ORDER_BY findById(int id) {
				for (ORDER_BY entry : ORDER_BY.values()) {
					if (entry.getId() == id) {
						return entry;
					}
				}
				return null;
			}
		}
	}
}
