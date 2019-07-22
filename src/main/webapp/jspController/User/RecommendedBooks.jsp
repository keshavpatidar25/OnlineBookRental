<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="dto.User"%>
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
	final Logger logger = Logger.getLogger("UserRecommendedBooksJsp");
	logger.info("Inside User Recommended Books Controller");
	try {
		User user = new User();
		List<Book> recommendedBooksList=null;
		String result;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonUser"),
				new TypeReference<HashMap<String, Object>>() {
				});
		
		user.setUserEmail((String)map.get("userEmail"));
		
		try{
			recommendedBooksList = new BookDao().getRecommendedBooksDetailsList(user);
			result="SUCCESS";
		} catch(SQLException e){
			result="ERROR : Database Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result="ERROR : Server Error.";
			logger.error("Exception : "+e.getMessage(),e);
		}
		
		map.clear();
		map.put("result",result);
		map.put("recommendedBooksList", recommendedBooksList);
		// convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>