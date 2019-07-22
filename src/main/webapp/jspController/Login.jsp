<%@page import="org.apache.log4j.Logger"%>
<%@page import="service.UserService"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="dto.User"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="dao.UserDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%
	/**Logger Object for logging purpose */
	final Logger logger = Logger.getLogger("LoginJsp");
	logger.info("Inside Login Controller");
	try {
		User user = new User();
		String result;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonUser"),
				new TypeReference<HashMap<String, Object>>() {
				});


		user.setUserEmail(map.get("userEmail").toString());
		user.setUserPass(map.get("userPass").toString());
		
		try{
			result = new UserService().login(user, session);
		} catch(SQLException e){
			result = "ERROR : Database Error";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result = "ERROR : Server Error";
			logger.error("Exception : "+e.getMessage(),e);
		}
		map.clear();
		
		map.put("result", result);
		if(result=="SUCCESS"){
			map.put("user", user);
		}
		
		//convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>


