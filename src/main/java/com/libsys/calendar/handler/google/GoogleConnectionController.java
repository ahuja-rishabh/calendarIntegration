package com.libsys.calendar.handler.google;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.libsys.calendar.util.Const;

@Controller
public class GoogleConnectionController {
	
	@Value("${google.client.redirectUri}")
	private String redirectURI;
    
	GoogleAuthorizationCodeFlow flow;
    
	@RequestMapping(value = "/login/google", method = RequestMethod.GET)
	public RedirectView googleConnectionStatus(HttpServletRequest request,@RequestParam String userId) throws Exception {
		return new RedirectView(authorize(userId));
	}
	
	private String authorize(String userId) throws Exception {

		final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		InputStream in = GoogleCalendarController.class.getResourceAsStream(Const.CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + Const.CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(Const.JSON_FACTORY, new InputStreamReader(in));

		FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new java.io.File(Const.TOKENS_DIRECTORY_PATH));

		// Build flow and trigger user authorization request.
		flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, Const.JSON_FACTORY,
				clientSecrets, Const.SCOPES)
						.setDataStoreFactory(fileDataStoreFactory)
						.setAccessType("offline").build();
			
//			if(flow.loadCredential(userId)!=null) {
//				return null;
//			}
		AuthorizationCodeRequestUrl authorizationUrl;
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI+"?userId="+userId);
		System.out.println("cal authorizationUrl->" + authorizationUrl);
		return authorizationUrl.build();
	}
	
	@RequestMapping(value="login/google",method=RequestMethod.GET,params= {"code","userId"})
	public ResponseEntity<String> googleConnectionCallBack(@RequestParam String code,@RequestParam String userId) throws IOException {
		TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI+"?userId="+userId).execute();
		Credential credential = flow.createAndStoreCredential(response, userId);
		return new ResponseEntity<>("Success",HttpStatus.OK);
		}
	
	
	
	@RequestMapping(value="test/model/view",method=RequestMethod.GET)
	public ModelAndView  getModelAndViewForParams(@RequestParam String userId){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setStatus(HttpStatus.OK);
		modelAndView.getModel().put("message", "Hello "+userId + " ,How are you doing today");
		modelAndView.setViewName("NewFile.html");
		return modelAndView;
		}
	}
