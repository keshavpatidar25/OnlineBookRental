<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.fasterxml.jackson.databind.JsonNode"%>
<%@page import="dao.RequestDao"%>
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
	final Logger logger = Logger.getLogger("OperatorCloseReturnJsp");
	logger.info("Inside Operator Close Return Controller");
	try {
		User user = new User();
		Book book = new Book();
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		
		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonBookRequest"),
				new TypeReference<HashMap<String, String>>() {
				});
		
		user.setUserEmail(map.get("userEmail"));
		book.setBookNo(Integer.parseInt(map.get("bookNo")));

		String result;
		try{
			result=new RequestDao().closeReturn(user, book);
		} catch(SQLException e){
			result = "ERROR : Database Error";
			logger.error("Exception : "+e.getMessage(),e);
		}
		
		map.clear();
		//convert map to JSON string
		map.put("result",result);
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();  
 
	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>


