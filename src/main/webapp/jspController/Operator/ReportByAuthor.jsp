<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.sql.SQLException"%>
<%@page import="dao.RequestDao"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="dto.Book"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="com.itextpdf.text.pdf.PdfWriter"%>
<%@page import="service.ReportService"%>
<%
	/**Logger Object for logging purpose */
	final Logger logger = Logger.getLogger("OperatorReportByAuthorJsp");
	logger.info("Inside Operator Report By Author Controller");
	try {
				
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

 	//convert JSON string to Map
		map = mapper.readValue(request.getParameter("jsonReport"),
				new TypeReference<HashMap<String, Object>>() {
				});
	 	try{
			Book book = new Book();
			book.setBookAuthor(map.get("requestAuthor").toString());
			String fromDate = map.get("fromDate").toString();
			String toDate = map.get("toDate").toString();
			map.clear(); 
			ByteArrayOutputStream os =new ByteArrayOutputStream();
			map.put("reportDetails",new ReportService().getReportByAuthor(book, fromDate, toDate, os));
			map.put("reportDetailsSum",new RequestDao().getReportsByAuthorSum(book, fromDate, toDate));
			byte[] pdfBytes = os.toByteArray();
			map.put("pdfBytes", pdfBytes);
			map.put("result","SUCCESS");
		} catch(SQLException e){
			map.put("result","ERROR : Database Error.");
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			map.put("result","ERROR : Server Error.");
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
