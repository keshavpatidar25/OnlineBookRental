<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dao.BookDao"%>
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
	final Logger logger = Logger.getLogger("SearchByTitleJsp");
	logger.info("Inside Search By Title Controller");
	try {
		String result;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>>booksList = new ArrayList<Map<String,Object>>();

		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonFilter"),
				new TypeReference<HashMap<String, Object>>() {
				});

		String filter = map.get("filter").toString();
		
		try{
			booksList = new BookDao().getBooksListByTitle(filter);
			result = "SUCCESS";
		} catch(SQLException e){
			result = "ERROR : Database Error";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result = "ERROR : Server Error";
			logger.error("Exception : "+e.getMessage(),e);
		}
		
		map.clear();
		
		map.put("booksList",booksList);
		map.put("result", result);
		
		//convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>