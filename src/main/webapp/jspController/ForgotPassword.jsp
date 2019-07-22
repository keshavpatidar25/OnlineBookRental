
<%@page import="org.apache.log4j.Logger"%>
<%@page import="javax.mail.SendFailedException"%>
<%@page import="service.MailService"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%
	/**Logger Object for logging purpose */
	final Logger logger = Logger.getLogger("ForgotPasswordJsp");
	logger.info("Inside Forgot Password Controller");
	try {
		String result;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();

		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonMail"),
				new TypeReference<HashMap<String, String>>() {
				});

		String mailSubject = map.get("mailSubject");
		String mailBody = map.get("mailBody");
		String mailToEmail = map.get("mailToEmail");
		
		try{
			result = new MailService(mailToEmail,mailSubject,mailBody).sendEmail();	
		} catch(Exception e){
			result = "Error Occured. Please try again";
			logger.error("Exception : "+e.getMessage(),e);
		}
		
		map.clear();
		
		map.put("result", result);
		
		//convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>


