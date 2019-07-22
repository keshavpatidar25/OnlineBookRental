<%@page import="org.apache.log4j.Logger"%>
<%@page import="dto.Book"%>
<%@page import="dao.BookDao"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%
/**Logger Object for logging purpose */
	final Logger logger = Logger.getLogger("CategoryListJsp");
	logger.info("Inside Category List Controller");
	try {
		response.addHeader("Access-Control-Allow-Origin", "*");
		List<String> categoryList = new BookDao().getBookCategories();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryList", categoryList);
		// convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>