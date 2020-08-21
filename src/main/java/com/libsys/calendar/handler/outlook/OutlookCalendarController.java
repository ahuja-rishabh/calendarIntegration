//package com.libsys.calendar.handler.outlook;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//
//import javax.naming.ServiceUnavailableException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.assertj.core.util.Arrays;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.libsys.calendar.util.HttpClientHelper;
//import com.microsoft.aad.msal4j.AuthorizationCodeParameters;
//import com.microsoft.aad.msal4j.ClientCredentialFactory;
//import com.microsoft.aad.msal4j.ConfidentialClientApplication;
//import com.microsoft.aad.msal4j.IAuthenticationResult;
////import com.microsoft.azure.msalwebsample.HttpClientHelper;
//import com.microsoft.graph.auth.confidentialClient.AuthorizationCodeProvider;
//import com.microsoft.graph.auth.confidentialClient.ClientCredentialProvider;
//import com.microsoft.graph.auth.enums.NationalCloud;
//import com.microsoft.graph.auth.publicClient.UsernamePasswordProvider;
//import com.microsoft.graph.authentication.IAuthenticationProvider;
//import com.microsoft.graph.models.extensions.Calendar;
//import com.microsoft.graph.models.extensions.IGraphServiceClient;
//import com.microsoft.graph.requests.extensions.GraphServiceClient;
//import com.microsoft.graph.requests.extensions.ICalendarCollectionPage;
//
//
//@RestController
//@RequestMapping("api/outlook/calendar")
//public class OutlookCalendarController {
//	@Value("${aad.clientId}")
//	private String clientId;
//	@Value("${aad.redirectUriSignin}")
//	private String redirectUri;
//	@Value("${aad.secretKey}")
//	private String clientSecret;
//	@Value("${aad.tenantId}")
//	private String tenantId;
//	@Value("${aad.authority}")
//	private String authority;
//	@Value("${aad.msGraphEndpointHost}")
//	private String msGraphEndpointHost;
//
//	@GetMapping("redirectUrl")
//	public String redirectEndPoint(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam String code) {
//		System.out.println(code);
//		fetchCalendars("rishabh_ahuja@outlook.com");
//    	String currentUri = httpServletRequest.getRequestURL().toString();
//
//		try {
//			IAuthenticationResult authToken=fetchTokenFromAuthCode(code,currentUri);
//			
//			if(authToken!=null) {
//				System.out.println(getUserInfoFromGraph(authToken.accessToken()));
//			}
//			
//		} catch ( Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "SUCCESS";
//	}
//	
//	
//	private IAuthenticationResult fetchTokenFromAuthCode(String code,String currentUri) throws Throwable {
//
//        IAuthenticationResult result;
//        ConfidentialClientApplication app;
//        try {
//            app = createClientApplication();
//
//            Set<String> scopes=new HashSet<>();
//            scopes.add("https://graph.microsoft.com/.default");
//            
//			AuthorizationCodeParameters parameters = AuthorizationCodeParameters.builder(
//                    code,
//                    new URI(currentUri)).scopes(scopes).
//                    build();
//
//            Future<IAuthenticationResult> future = app.acquireToken(parameters);
//
//            result = future.get();
//        } catch (ExecutionException e) {
//            throw e.getCause();
//        }
//        
//        if (result == null) {
//            throw new ServiceUnavailableException("authentication result was null");
//        }
//
//		return result;
//	}
//
//
//	 private ConfidentialClientApplication createClientApplication() throws MalformedURLException {
//	        return ConfidentialClientApplication.builder(clientId, ClientCredentialFactory.createFromSecret(clientSecret)).
//	                authority(authority).
//	                build();
//	    }
//
//
//	private String fetchCalendars(String userId){
//		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider( getAuthenticationProvider(userId) ).buildClient();
//
//		ICalendarCollectionPage calendars = graphClient.users(userId
//				).calendars()
//			.buildRequest()
//			.get();
//		return calendars.getRawObject().toString();
//	}
//
//
//	private IAuthenticationProvider getAuthenticationProvider(String code) {
//		List<String> scopes= new ArrayList();
//		scopes.add("https://graph.microsoft.com/.default");
////		scopes.add("Calendars.Read");
////		scopes.add("Calendars.ReadWrite");
////		AuthorizationCodeProvider authorizationCodeProvider = new AuthorizationCodeProvider(clientId,scopes, code, redirectUri,NationalCloud.Global,tenantId, clientSecret); 
//		ClientCredentialProvider clientCredentialProvider = new ClientCredentialProvider(clientId, scopes, clientSecret, tenantId, NationalCloud.Global);
////		UsernamePasswordProvider usernamePasswordProvider = new UsernamePasswordProvider(clientId, scopes, "deepak@libsys.in", "libsys@123",NationalCloud.Global,tenantId,clientSecret);
//		return clientCredentialProvider;
//	}
//	
//	private String getUserInfoFromGraph(String accessToken) throws Exception {
//        // Microsoft Graph user endpoint
//		String userId="c51ffb85-0785-4670-b14e-7b3a5e961788";
//        URL url = new URL(msGraphEndpointHost + "v1.0/users/"+userId+"/calendars");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        // Set the appropriate header fields in the request header.
//        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
//        conn.setRequestProperty("Accept", "application/json");
//
//        String response = HttpClientHelper.getResponseStringFromConn(conn);
//
//        int responseCode = conn.getResponseCode();
//        if(responseCode != HttpURLConnection.HTTP_OK) {
//            throw new IOException(response);
//        }
//
//        JSONObject responseObject = HttpClientHelper.processResponse(responseCode, response);
//        return responseObject.toString();
//    }
//	
//}
