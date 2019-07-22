<%@page import="org.apache.log4j.Logger"%>
<%@page import="dto.Review"%>
<%@page import="dto.Rating"%>
<%@page import="dao.ReviewDao"%>
<%@page import="dao.RatingDao"%>
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
	final Logger logger = Logger.getLogger("UserAddReviewJsp");
	logger.info("Inside User Add Review Controller");
	try {
		Review review = new Review();
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonReview"),
				new TypeReference<HashMap<String, Object>>() {
				});
		
		review.setUserEmail(map.get("userEmail").toString());
		review.setBookNo(Integer.parseInt(map.get("bookNo").toString()));
		review.setBookReview(map.get("bookReview").toString());
		map.clear();
		try{
			map.put("result", new ReviewDao().addReview(review));
		} catch(SQLException e){
			map.put("result", "ERROR : Database Error");
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			map.put("result", "ERROR : Server Error");
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