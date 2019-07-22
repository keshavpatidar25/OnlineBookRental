<%@page import="org.apache.log4j.Logger"%>
<%@page import="service.BookService"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="dao.BookDao"%>
<%@page import="dto.Book"%>
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
	final Logger logger = Logger.getLogger("OperatorUpdateBookJsp");
	logger.info("Inside Operator Update Book Controller");
	try {
		InputStreamReader inputstream =  new InputStreamReader(request.getInputStream());
		BufferedReader br=new BufferedReader(inputstream);
	   	String jsonBook="";
	    String line="";
	    while((line=br.readLine() )!=null){
		    jsonBook+=line;
		}
		Book book = new Book();
		String result;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();

		//convert JSON string to Map
		map = mapper.readValue(jsonBook,
				new TypeReference<HashMap<String, String>>() {
				});
		book.setBookNo(Integer.parseInt(map.get("bookNo")));
		book.setBookTitle(map.get("bookTitle"));
		book.setBookAuthor(map.get("bookAuthor"));
		book.setBookCategory(map.get("bookCategory"));
		book.setBookPublisher(map.get("bookPublisher"));
		book.setBookCover(Base64.decodeBase64(map.get("bookCover").getBytes()));
		book.setBookQty(Integer.parseInt(map.get("bookQty")));
		book.setBookPrice(Float.parseFloat(map.get("bookPrice")));
		book.setBookDescription(map.get("bookDescription"));
			
		try{
			result=new BookService().checkAvailabilityBeforeUpdate(book);
		} catch(SQLException e){
			result = "ERROR : Database Error";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result = "ERROR : Server Error";
			logger.error("Exception : "+e.getMessage(),e);
		}
		
		map.clear();
		
		map.put("result", result);
		
		//convert map to JSON string
		String jsonResponse = mapper.writeValueAsString(map);
		out.print(jsonResponse);
		out.flush();

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>