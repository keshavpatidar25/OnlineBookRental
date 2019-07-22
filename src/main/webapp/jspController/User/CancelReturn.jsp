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
	final Logger logger = Logger.getLogger("UserCancelReturnJsp");
	logger.info("Inside User Cancel Return Controller");
	try {
		User user = new User();
		Book book = new Book();
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		
		JsonNode bookRequestNode = mapper.readTree(request.getParameter("jsonBookRequest"));
		JsonNode userNode = bookRequestNode.path("user");
		JsonNode bookNode = bookRequestNode.path("bookNo");
		
		Map<String,Object> userDetails = new HashMap<String,Object>();
		userDetails = mapper.readValue(userNode.toString(),
				new TypeReference<HashMap<String, Object>>() {
				}); 
			
		user.setUserEmail(userDetails.get("userEmail").toString());
		user.setUserRole(userDetails.get("userRole").toString());
		book.setBookNo(bookNode.asInt());

		String result;
		try{
			result=new RequestDao().cancelReturn(user, book);
		} catch(SQLException e){
			result = "ERROR : Database Error";
			logger.error("Exception : "+e.getMessage(),e);
		}
		//convert map to JSON string
		map.put("result",result);
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();  
 
	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>