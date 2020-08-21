package com.libsys.calendar.service;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.libsys.calendar.handler.google.GoogleCalendarController;
import com.libsys.calendar.util.Const;

@Service
public class Authorization {


	/**
	 * 
	 * @param emailId
	 * @param scopes
	 * @param HTTP_TRANSPORT
	 * @return
	 * @throws IOException
	 */
	private static Credential getCredentials(String userId, List<String> scopes, final NetHttpTransport HTTP_TRANSPORT)
			throws IOException {
		// Load client secrets.
		InputStream in = GoogleCalendarController.class.getResourceAsStream(Const.CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + Const.CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(Const.JSON_FACTORY, new InputStreamReader(in));

		FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new java.io.File(Const.TOKENS_DIRECTORY_PATH));
		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, Const.JSON_FACTORY,
				clientSecrets, scopes).setDataStoreFactory(fileDataStoreFactory).setAccessType("offline").build();
		if (flow.loadCredential(userId) != null) {
			return flow.loadCredential(userId);
		}
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize(userId);
	}

	/**
	 * 
	 * @param applicationName
	 * @param userId
	 * @param scopes
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public Calendar getCalendarService(String applicationName, String userId, List<String> scopes)
			throws IOException, GeneralSecurityException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Calendar service = new Calendar.Builder(HTTP_TRANSPORT, Const.JSON_FACTORY,
				getCredentials(userId, scopes, HTTP_TRANSPORT)).setApplicationName(applicationName).build();
		return service;
	}

}
