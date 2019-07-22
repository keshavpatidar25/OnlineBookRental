<%@page import="org.apache.log4j.Logger"%>
<%@page import="dao.SubscriptionPlanDao"%>
<%@page import="dto.SubscriptionPlan"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="dto.User"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%
	/**Logger Object for logging purpose */
	final Logger logger = Logger.getLogger("UserNewSubscriptionJsp");
	logger.info("Inside User New Subscription Controller");
	try {
		User user = new User();
		SubscriptionPlan plan = new SubscriptionPlan();
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();

		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonUserPlan"),
				new TypeReference<HashMap<String, String>>() {
				});
			
		user.setUserEmail(map.get("userEmail"));
		plan.setPlanId(map.get("planId"));
		
		String result;
		try{
			result = new SubscriptionPlanDao().addPlan(plan, user);
		} catch(SQLException e){
			result = "ERROR : Database Error";
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


