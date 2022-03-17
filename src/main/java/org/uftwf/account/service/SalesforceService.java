package org.uftwf.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.uftwf.account.model.ApiUser;
import org.uftwf.account.model.CallResponse;
import org.uftwf.account.model.EmailOptOutFlag;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.util.HashMap;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SalesforceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceService.class);

    public CallResponse getEmailOptOutStatus(String memberId){
        RestTemplate restTemplate =new RestTemplate();
        String token = null;
        try{
            token = getApiAccessToken();
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            String postParameter = "{\"mbsid\":\""+memberId+"\"}";
            HttpEntity<String> entity = new HttpEntity<String>(postParameter,headers);
            InitialContext ctx = new InitialContext();
            String url=(String)ctx.lookup("java:global/uft/host");
            String apiUrl = url+"/salesforceAPI/rest/email_opt_out_status";
            ResponseEntity responseEntity = template.exchange(apiUrl,HttpMethod.POST,entity,String.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK){
                String responseBody = (String) responseEntity.getBody();
                HashMap callbackResponse = new ObjectMapper().readValue(responseBody, HashMap.class);

                return new CallResponse((boolean)callbackResponse.get("status"),callbackResponse.get("message"));
            }
            return new CallResponse(false,  "api call error");
        }catch (Exception e){
            e.printStackTrace();
            return new CallResponse(false,  "api call error");
        }
    }
    public CallResponse updateEmailOptOut(String memberId, boolean flag){
        RestTemplate restTemplate =new RestTemplate();
        String token = null;
        try{
            token = getApiAccessToken();
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            EmailOptOutFlag emailflag = new EmailOptOutFlag(memberId,flag);
            String postParameter = new ObjectMapper().writeValueAsString(emailflag);
            HttpEntity<String> entity = new HttpEntity<String>(postParameter,headers);
            InitialContext ctx = new InitialContext();
            String url=(String)ctx.lookup("java:global/uft/host");
            String apiUrl = url+"/salesforceAPI/rest/update_email_opt_out_status";
            ResponseEntity responseEntity = template.exchange(apiUrl,HttpMethod.POST,entity,String.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK){
                String responseBody = (String) responseEntity.getBody();
                HashMap callbackResponse = new ObjectMapper().readValue(responseBody, HashMap.class);

                return new CallResponse((boolean)callbackResponse.get("status"),callbackResponse.get("message"));
            }
            return new CallResponse(false, (Object) "api call error");
        }catch (Exception e){
            e.printStackTrace();
            return new CallResponse(false, (Object) "api call error");
        }
    }
    private String getApiAccessToken() throws IOException, NamingException {
        String username = System.getProperty("salesforceApiUsername");
        String password = System.getProperty("salesforceApiPassword");

        InitialContext ctx = new InitialContext();
        String url=(String)ctx.lookup("java:global/uft/host");
        String apiUrl = url+"/salesforceAPI/authenticate";
        ApiUser user = new ApiUser(username,password);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String postParameter = new ObjectMapper().writeValueAsString(user);
        HttpEntity<String> entity = new HttpEntity<String>(postParameter,headers);
        ResponseEntity responseEntity= restTemplate.exchange(apiUrl, HttpMethod.POST,entity,String.class);
        if(responseEntity.getStatusCode() == HttpStatus.OK){
             String responseJson =(String) responseEntity.getBody();
             HashMap responseMap = new ObjectMapper().readValue(responseJson, HashMap.class);
             return (String) responseMap.get("jwttoken");
        }
        return null;

    }

}
