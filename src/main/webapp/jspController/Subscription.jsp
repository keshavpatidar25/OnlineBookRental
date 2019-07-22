<%@page import="org.apache.log4j.Logger"%>
<%@page import="dao.SubscriptionPlanDao"%>
<%@page import="dto.SubscriptionPlan"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%
	/**Logger Object for logging purpose */
	final Logger logger = Logger.getLogger("SubscriptionJsp");
	logger.info("Inside Subscription Controller");
	try {
		List<SubscriptionPlan> planList = new SubscriptionPlanDao().getAllPlans();
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, List<SubscriptionPlan>> map = new HashMap<String, List<SubscriptionPlan>>();
		map.put("planList", planList);
		//convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>