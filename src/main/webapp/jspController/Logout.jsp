<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="service.SessionService"%>
<%	
	/**Logger Object for logging purpose */
	final Logger logger = Logger.getLogger("LogoutJsp");
	logger.info("Inside Logout Controller");
	try{ 
		String result=null;
		try{
			SessionService.clearSession(session);
			result="SUCCESS";
		} catch(Exception e){
			result="ERROR";
			logger.error("Exception : "+e.getMessage(),e);
		}
		Map<String,String> map = new HashMap<String,String>();
		ObjectMapper mapper = new ObjectMapper();
		map.put("result", result);
		//convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();
		
	} catch(Exception e){
		logger.error("Exception : "+e.getMessage(),e);
	}
%>