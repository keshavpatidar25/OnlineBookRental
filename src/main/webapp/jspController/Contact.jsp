
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
	final Logger logger = Logger.getLogger("ContactJsp");
	logger.info("Inside Contact Controller");
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
		String mailFromEmail = map.get("mailFromEmail");
		
		try{
			result = new MailService(mailSubject, mailBody).sendEmail();	
		} catch(Exception e){
			result = "Feedback could not be sent. Server Error.";
		}
		
		map.clear();
		
		map.put("result", result);
		
		//convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();
					
		new MailService().thanksEmail(mailFromEmail);

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>


