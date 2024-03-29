<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%@page import="dao.BookDao"%>
<%@page import="dto.Book"%>
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
	final Logger logger = Logger.getLogger("UserCurrentlyHeldBooksJsp");
	logger.info("Inside User Currently Held Books Controller");
	try {
		User user = new User();
		List<Map<String,Object>> issuedBooksList;
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonUser"),
				new TypeReference<HashMap<String, Object>>() {
				});
		
		user.setUserEmail((String)map.get("userEmail"));
		map.clear();		
		String result;
		try{
			issuedBooksList = new BookDao().getIssuedBooksDetails(user);
			map.put("issuedBooksList",issuedBooksList);
			map.put("result","SUCCESS");
		} catch(SQLException e){
			map.put("result", "ERROR : Database Error");
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			map.put("result","ERROR : Server Error");
			logger.error("Exception : "+e.getMessage(),e);
		}

		//convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();  

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>


