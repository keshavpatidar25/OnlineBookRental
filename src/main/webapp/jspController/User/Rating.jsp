<%@page import="org.apache.log4j.Logger"%>
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
	final Logger logger = Logger.getLogger("UserRatingJsp");
	logger.info("Inside User Rating Controller");
	try {
		Book book = new Book();
		User user = new User();
		RatingDao ratingDao = new RatingDao();
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonBook"),
				new TypeReference<HashMap<String, Object>>() {
				});
		
		user.setUserEmail(map.get("userEmail").toString());
		book.setBookNo(Integer.parseInt(map.get("bookNo").toString()));
		map.clear();
		try{
			map.put("ratingDetails", ratingDao.getRatingDetails(book));
			map.put("userBookRating", ratingDao.getBookRating(user,book));
			map.put("bookReviewsList", new ReviewDao().getBookReviews(book));
			map.put("result","SUCCESS");
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


