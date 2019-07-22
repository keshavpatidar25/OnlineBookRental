
<%@page import="org.apache.log4j.Logger"%>
<%@page import="dao.BookDao"%>
<%@page import="dto.Book"%>
<%@page import="javax.mail.SendFailedException"%>
<%@page import="service.MailService"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%
	/**Logger Object for logging purpose */
	final Logger logger = Logger.getLogger("MailJsp");
	logger.info("Inside Mail Controller");
	try {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();

		//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonMail"),
				new TypeReference<HashMap<String, String>>() {
				});
		
		String mailTo = map.get("mailTo");
		String mailSubject = map.get("mailSubject");
		String mailBody = map.get("mailBody");
		Book book= new Book();
		book.setBookNo(Integer.parseInt(map.get("bookNo")));
		new BookDao().getBookDetails(book);
		new MailService(mailTo, mailSubject, mailBody).addBookDetailsInMail(book);			

	} catch (Exception e) {		
		logger.error("Exception : "+e.getMessage(),e);
	}
%>


