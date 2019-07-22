package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import conn.MyConnection;
import dto.Book;
import dto.Request;
import dto.SubRequest;
import dto.User;


/**
 * The Class RequestDao.
 */
public class RequestDao {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(RequestDao.class);
	
	/** Database connection singleton class object. */
	private MyConnection conn = new MyConnection();
	
	/**
	 * Issue book request.
	 *
	 * @param user the user who wants to issue book
	 * @param book the book which is to be issued
	 * @return the status of the operation whether success of failure
	 * @throws SQLException the SQL exception
	 */
	public String issueBook(User user, Book book) throws SQLException{
		logger.info("Issuing Book");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			/**Insert record into book_request table.*/
			PreparedStatement ps = con.prepareStatement("INSERT INTO book_request(user_email,book_no,delivery_status) values(?,?,'PENDING')");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			ps.executeUpdate();
			
			/**Get request_no from book_request table for inserting record into book_sub_request table.*/
			ps = con.prepareStatement("SELECT request_no from book_request where user_email=? and book_no=? and delivery_status='PENDING'");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			/**Insert record into book_sub_request table.*/
			ps = con.prepareStatement("INSERT INTO book_sub_request(request_no,request_type,request_sub_type,request_date,address,contact,request_by) values(?,'DELIVERY','PENDING',?,?,?,?)");
			ps.setInt(1,rs.getInt("request_no"));
			ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
			ps.setString(3, user.getUserAddress());
			ps.setString(4, user.getUserContact());
			ps.setString(5, user.getUserRole());
			ps.executeUpdate();
			result="SUCCESS";
		} catch(SQLException e){
			result="ERROR : Database Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result="ERROR : Server Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Cancel issue book request.
	 *
	 * @param user the user whose issue book request is to be cancelled
	 * @param book the book whose issue book request is to be cancelled
	 * @return the status of the operation whether success of failure
	 * @throws SQLException the SQL exception
	 */
	public String cancelIssue(User user, Book book) throws SQLException{
		logger.info("Cancelling Book Issue");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			/**Get request_no of request to be cancelled.*/
			PreparedStatement ps = con.prepareStatement("SELECT request_no FROM book_request WHERE user_email=? AND book_no=? AND delivery_status='PENDING'");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){	
				/**Update book_request table.*/
				ps = con.prepareStatement("UPDATE book_request SET delivery_status='CANCELLED' where request_no=?");
				ps.setInt(1,rs.getInt("request_no"));
				ps.executeUpdate();
				
				/**Insert record into book_sub_request table.*/
				ps = con.prepareStatement("INSERT INTO book_sub_request(request_no,request_type,request_sub_type,request_date,request_by) values(?,'DELIVERY','CANCELLED',?,?)");
				ps.setInt(1,rs.getInt("request_no"));
				ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				ps.setString(3, user.getUserRole());
				ps.executeUpdate();
				result="SUCCESS";
			} else{
				result="Book Issue Cannot be Cancelled.\nYour request may have been modified.\nPlease refresh Page to verify";
			}
		} catch(SQLException e){
			result="Book Issue cannot be Cancelled. ERROR : Database Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result="Book Issue cannot be Cancelled. ERROR : Server Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Close issue book request.
	 *
	 * @param user the user whose issue book request is to be closed
	 * @param book the book whose issue book request is to be closed
	 * @return the status of the operation whether success of failure
	 * @throws SQLException the SQL exception
	 */
	public String closeIssue(User user, Book book) throws SQLException{
		logger.info("Closing Book Issue");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			
			/**Get request_no of request to be closed.*/
			PreparedStatement ps = con.prepareStatement("SELECT request_no FROM book_request WHERE user_email=? AND book_no=? AND delivery_status='PENDING'");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){	
				/**Update book_request table.*/
				ps = con.prepareStatement("UPDATE book_request SET delivery_status='CLOSED' where request_no=?");
				ps.setInt(1,rs.getInt("request_no"));
				ps.executeUpdate();
				
				/**Insert record into book_sub_request table.*/
				ps = con.prepareStatement("INSERT INTO book_sub_request(request_no,request_type,request_sub_type,request_date,request_by) values(?,'DELIVERY','CLOSED',?,'OPERATOR')");
				ps.setInt(1,rs.getInt("request_no"));
				ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				ps.executeUpdate();
				result="SUCCESS";
			}  else{
				result="Book Issue Cannot be Closed.\nYour request may have been modified.\nPlease refresh Page to verify";
			}
		} catch(SQLException e){
			result="Book Issue cannot be Closed. ERROR : Database Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result="Book Issue cannot be Closed. ERROR : Server Error.";
			logger.error("Exception : "+e.getMessage(),e);
		}  finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Return book request.
	 *
	 * @param user the user who wants to return the book
	 * @param book the book which is to be returned
	 * @return the status of the operation whether success of failure
	 * @throws SQLException the SQL exception
	 */
	public String returnBook(User user, Book book) throws SQLException{
		logger.info("Returning Book");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			
			/**Get request_no of request related to the book to be returned.*/
			PreparedStatement ps = con.prepareStatement("SELECT request_no FROM book_request WHERE user_email=? AND book_no=? AND delivery_status='CLOSED' AND return_status NOT IN('PENDING','CLOSED')");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){	
				
				/**Update book_request table.*/
				ps = con.prepareStatement("UPDATE book_request SET return_status='PENDING' where request_no=?");
				ps.setInt(1,rs.getInt("request_no"));
				ps.executeUpdate();
				
				/**Insert record into book_sub_request table.*/
				ps = con.prepareStatement("INSERT INTO book_sub_request(request_no,request_type,request_sub_type,request_date,address,contact,request_by) values(?,'RETURN','PENDING',?,?,?,?)");
				ps.setInt(1,rs.getInt("request_no"));
				ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				ps.setString(3, user.getUserAddress());
				ps.setString(4, user.getUserContact());
				ps.setString(5, user.getUserRole());
				ps.executeUpdate();
				result="SUCCESS";
			} else{
				result="Book Cannot be Returned.\nYour request may have been modified.\nPlease refresh Page to verify";
			}
		} catch(SQLException e){
			result="Book Return cannot be Initiated. ERROR : Database Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result="Book Return cannot be Initiated. ERROR : Server Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Cancel return request.
	 *
	 * @param user the user whose return book request is to be cancelled
	 * @param book the book whose return book request is to be cancelled
	 * @return the status of the operation whether success of failure
	 * @throws SQLException the SQL exception
	 */
	public String cancelReturn(User user, Book book) throws SQLException{
		logger.info("Cancelling Book Return");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			
			/**Get request_no of request related to the book whose return request is to be cancelled.*/
			PreparedStatement ps = con.prepareStatement("SELECT request_no FROM book_request WHERE user_email=? AND book_no=? AND return_status='PENDING'");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){	
			
				/**Update book_request table.*/
				ps = con.prepareStatement("UPDATE book_request SET return_status='CANCELLED' where request_no=?");
				ps.setInt(1,rs.getInt("request_no"));
				ps.executeUpdate();
				
				/**Insert record into book_sub_request table.*/
				ps = con.prepareStatement("INSERT INTO book_sub_request(request_no,request_type,request_sub_type,request_date,request_by) values(?,'RETURN','CANCELLED',?,?)");
				ps.setInt(1,rs.getInt("request_no"));
				ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				ps.setString(3, user.getUserRole());
				ps.executeUpdate();
				result="SUCCESS";
			} else{
				result="Book Return Cannot be Cancelled.\nYour request may have been modified.\nPlease refresh Page to verify";
			}
		} catch(SQLException e){
			result="Book Return cannot be Cancelled. ERROR : Database Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result="Book Return cannot be Cancelled. ERROR : Server Error.";
			logger.error("Exception : "+e.getMessage(),e);
		}  finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Close return request.
	 *
	 * @param user the user whose return book request is to be closed
	 * @param book the book whose return book request is to be closed
	 * @return the status of the operation whether success of failure
	 * @throws SQLException the SQL exception
	 */
	public String closeReturn(User user, Book book) throws SQLException{
		logger.info("Closing Book Return");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			
			/**Get request_no of request related to the book whose return request is to be closed.*/
			PreparedStatement ps = con.prepareStatement("SELECT request_no FROM book_request WHERE user_email=? AND book_no=? AND return_status='PENDING'");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
			
				/**Update book_request table.*/
				ps = con.prepareStatement("UPDATE book_request SET return_status='CLOSED' where request_no=?");
				ps.setInt(1,rs.getInt("request_no"));
				ps.executeUpdate();
				
				/**Insert record into book_sub_request table.*/
				ps = con.prepareStatement("INSERT INTO book_sub_request(request_no,request_type,request_sub_type,request_date,request_by) values(?,'RETURN','CLOSED',?,'OPERATOR')");
				ps.setInt(1,rs.getInt("request_no"));
				ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				ps.executeUpdate();
				result="SUCCESS";
			} else{
				result="Book Return Cannot be Closed.\nYour request may have been modified.\nPlease refresh Page to verify";
			}
		} catch(SQLException e){
			result="Book Return cannot be Closed. ERROR : Database Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result="Book Return cannot be Closed. ERROR : Server Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Gets the books request history.
	 *
	 * @param user the user whose book request history is to be found
	 * @param fromDate the from date indicates from which date user history is to be found
	 * @param toDate the to date indicates to which date user history is to be found
	 * @return the books request history
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String, Object>> getBooksRequestHistory(User user, String fromDate, String toDate) throws SQLException, Exception{
		logger.info("Getting Books Request History");
		Connection con = null;
		Request request = null;
		Book book = null;
		List<Map<String, Object>> booksHistoryList=null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			booksHistoryList = new ArrayList<Map<String, Object>>();
			PreparedStatement ps = con
					.prepareStatement("SELECT br.*, request_date FROM book_request br INNER JOIN book_sub_request bsr ON br.request_no= bsr.request_no WHERE user_email = ? AND (request_date BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY)) AND (br.delivery_status='CANCELLED' OR br.return_status='CLOSED') AND bsr.request_type = 'DELIVERY' AND bsr.request_sub_type='PENDING'");
			ps.setString(1, user.getUserEmail());
			ps.setString(2, fromDate);
			ps.setString(3, toDate);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap<String, Object>();
				request = new Request();
				book = new Book();
				request.setBookNo(rs.getInt("book_no"));
				book.setBookNo(request.getBookNo());
				book = new BookDao().getBookDetails(book);
				request.setDeliveryStatus(rs.getString("delivery_status"));
				request.setReturnStatus(rs.getString("return_status"));
				request.setRequestNo(rs.getInt("request_no"));
				Timestamp requestDate = rs.getTimestamp("request_date");
				map.put("request", request);
				map.put("book", book);
				map.put("requestDate", requestDate);
				booksHistoryList.add(map);
			}
		} finally {
			if (con != null)
				con.close();
		}
		return booksHistoryList;
	}
	
	/**
	 * Gets the all requests.
	 *
	 * @return the all requests
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String, Object>> getAllRequests() throws SQLException, Exception{
		logger.info("Getting All Requests");
		Connection con = null;
		Request request = null;
		SubRequest subRequest = null;
		Book book = null;
		List<Map<String, Object>> requestsList=null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			requestsList = new ArrayList<Map<String, Object>>();
			PreparedStatement ps = con.prepareStatement("SELECT br.*,book_title FROM book_request br INNER JOIN book b ON br.book_no=b.book_no ORDER BY request_no DESC LIMIT 1000");
			PreparedStatement ps1;
			ResultSet rs = ps.executeQuery();
			ResultSet rs1;
			while(rs.next()){
				map = new HashMap<String, Object>();
				request = new Request();
				book = new Book();
				subRequest = new SubRequest();
				/** First get request details */
				book.setBookNo(rs.getInt("book_no"));
				book.setBookTitle(rs.getString("book_title"));				
				request.setUserEmail(rs.getString("user_email"));
				request.setDeliveryStatus(rs.getString("delivery_status"));
				request.setReturnStatus(rs.getString("return_status"));
				request.setRequestNo(rs.getInt("request_no"));
				/** Check whether Delivery Status or Return Status is Pending.
				 * If true, then also get the address and contact no. associated with the request.				 * 
				 */
				if(request.getDeliveryStatus().equals("PENDING")){
					ps1=con.prepareStatement("SELECT address,contact FROM book_sub_request WHERE request_no=? AND request_type='DELIVERY' AND request_sub_type='PENDING'");
					ps1.setInt(1, request.getRequestNo());
					rs1=ps1.executeQuery();
					if(rs1.next()){
						subRequest.setSubRequestAddress(rs1.getString("address"));
						subRequest.setSubRequestContact(rs1.getString("contact"));
					} 
				}else if(request.getReturnStatus().equals("PENDING")){
					ps1=con.prepareStatement("SELECT address,contact FROM book_sub_request WHERE request_no=? AND request_type='RETURN' AND request_sub_type='PENDING' ORDER BY request_date DESC LIMIT 1");
					ps1.setInt(1, request.getRequestNo());
					rs1=ps1.executeQuery();
					if(rs1.next()){		
						subRequest.setSubRequestAddress(rs1.getString("address"));
						subRequest.setSubRequestContact(rs1.getString("contact"));
					}
				}else{
					subRequest.setSubRequestAddress("");
					subRequest.setSubRequestContact("");
				}
				map.put("request", request);
				map.put("book", book);
				map.put("subRequest",subRequest);
				requestsList.add(map);
			}
		} catch(SQLException e){
			logger.error("Exception : "+e.getMessage(),e);
		} finally {
			if (con != null)
				con.close();
		}
		return requestsList;
	}
	
	/**
	 * Gets the report.
	 *
	 * @param fromDate the date from which report is to be generated
	 * @param toDate the date to which report is to be generated
	 * @return the report
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getReport(String fromDate,String toDate) throws SQLException, Exception{
		logger.info("Getting Report");
		Connection con = null;
		Book book = null;
		List<Map<String, Object>> booksReport=null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			booksReport = new ArrayList<Map<String, Object>>();
			/**SUM(CASE WHEN) is used of counting requests (like issue request, issue cancel request, etc.)
			 *In this IF condition is satisfied than 1 is added to current count ELSE 0 is added to current count
			 *Hence, in the end we will get correct count related to respective request type
			 */
			PreparedStatement ps = con.prepareStatement("SELECT b.book_no, book_title, book_author, book_category, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) issue_pending, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) issue_cancelled, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) issue_closed, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) return_pending, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) return_cancelled, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) return_closed  FROM book b INNER JOIN book_request br ON b.book_no=br.book_no INNER JOIN book_sub_request bsr ON br.request_no=bsr.request_no WHERE request_date BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY) GROUP BY b.book_no");
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap<String, Object>();
				book = new Book();
				book.setBookNo(rs.getInt("book_no"));
				book.setBookTitle(rs.getString("book_title"));
				book.setBookAuthor(rs.getString("book_author"));
				book.setBookCategory(rs.getString("book_category"));
				map.put("issuePending", rs.getInt("issue_pending"));
				map.put("issueCancelled", rs.getInt("issue_cancelled"));
				map.put("issueClosed", rs.getInt("issue_closed"));
				map.put("returnPending", rs.getInt("return_pending"));
				map.put("returnCancelled", rs.getInt("return_cancelled"));
				map.put("returnClosed", rs.getInt("return_closed"));
				map.put("book", book);
				booksReport.add(map);
			}
			
		} finally {
			if (con != null)
				con.close();
		}
		return booksReport;
	}
	
	/**
	 * Gets the report on basis of title.
	 *
	 * @param b the book whose title is to be matched to generate report
	 * @param fromDate the date from which report is to be generated
	 * @param toDate the date to which report is to be generated
	 * @return the report on basis of book title
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getReportByTitle(Book b, String fromDate,String toDate) throws SQLException, Exception{
		logger.info("Getting Report By Title");
		Connection con = null;
		Book book = null;
		List<Map<String, Object>> booksReportByTitle=null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			booksReportByTitle = new ArrayList<Map<String, Object>>();
			/**SUM(CASE WHEN) is used of counting requests (like issue request, issue cancel request, etc.)
			 *In this IF condition is satisfied than 1 is added to current count ELSE 0 is added to current count
			 *Hence, in the end we will get correct count related to respective request type
			 */
			PreparedStatement ps = con.prepareStatement("SELECT b.book_no, book_title, book_author, book_category, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) issue_pending, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) issue_cancelled, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) issue_closed, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) return_pending, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) return_cancelled, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) return_closed  FROM book b INNER JOIN book_request br ON b.book_no=br.book_no INNER JOIN book_sub_request bsr ON br.request_no=bsr.request_no WHERE book_title LIKE ? AND request_date BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY) GROUP BY b.book_no");
			ps.setString(1, "%" + b.getBookTitle() + "%");
			ps.setString(2, fromDate);
			ps.setString(3, toDate);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap<String, Object>();
				book = new Book();
				book.setBookNo(rs.getInt("book_no"));
				book.setBookTitle(rs.getString("book_title"));
				book.setBookAuthor(rs.getString("book_author"));
				book.setBookCategory(rs.getString("book_category"));
				map.put("issuePending", rs.getInt("issue_pending"));
				map.put("issueCancelled", rs.getInt("issue_cancelled"));
				map.put("issueClosed", rs.getInt("issue_closed"));
				map.put("returnPending", rs.getInt("return_pending"));
				map.put("returnCancelled", rs.getInt("return_cancelled"));
				map.put("returnClosed", rs.getInt("return_closed"));
				map.put("book", book);
				booksReportByTitle.add(map);
			}
			
		} finally {
			if (con != null)
				con.close();
		}
		return booksReportByTitle;
	}
	
	/**
	 * Gets the report on basis of author.
	 *
	 * @param b the book whose author is to be matched to generate report
	 * @param fromDate the date from which report is to be generated
	 * @param toDate the date to which report is to be generated
	 * @return the report on basis of book author
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getReportByAuthor(Book b, String fromDate,String toDate) throws SQLException, Exception{
		logger.info("Getting Report By Author");
		Connection con = null;
		Book book = null;
		List<Map<String, Object>> booksReportByAuthor=null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			booksReportByAuthor = new ArrayList<Map<String, Object>>();
			/**SUM(CASE WHEN) is used of counting requests (like issue request, issue cancel request, etc.)
			 *In this IF condition is satisfied than 1 is added to current count ELSE 0 is added to current count
			 *Hence, in the end we will get correct count related to respective request type
			 */
			PreparedStatement ps = con.prepareStatement("SELECT b.book_no, book_title, book_category, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) issue_pending, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) issue_cancelled, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) issue_closed, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) return_pending, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) return_cancelled, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) return_closed  FROM book b INNER JOIN book_request br ON b.book_no=br.book_no INNER JOIN book_sub_request bsr ON br.request_no=bsr.request_no WHERE book_author = ? AND request_date BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY) GROUP BY b.book_no");
			ps.setString(1, b.getBookAuthor());
			ps.setString(2, fromDate);
			ps.setString(3, toDate);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap<String, Object>();
				book = new Book();
				book.setBookNo(rs.getInt("book_no"));
				book.setBookTitle(rs.getString("book_title"));
				book.setBookCategory(rs.getString("book_category"));
				map.put("issuePending", rs.getInt("issue_pending"));
				map.put("issueCancelled", rs.getInt("issue_cancelled"));
				map.put("issueClosed", rs.getInt("issue_closed"));
				map.put("returnPending", rs.getInt("return_pending"));
				map.put("returnCancelled", rs.getInt("return_cancelled"));
				map.put("returnClosed", rs.getInt("return_closed"));
				map.put("book", book);
				booksReportByAuthor.add(map);
			}
			
		} finally {
			if (con != null)
				con.close();
		}
		return booksReportByAuthor;
	}
	
	/**
	 * Gets the report on basis of category.
	 *
	 *@param b the book whose category is to be matched to generate report
	 * @param fromDate the date from which report is to be generated
	 * @param toDate the date to which report is to be generated
	 * @return the report on basis of book category
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getReportByCategory(Book b, String fromDate,String toDate) throws SQLException, Exception{
		logger.info("Getting Report By Category");
		Connection con = null;
		Book book = null;
		List<Map<String, Object>> booksReportByCategory=null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			booksReportByCategory = new ArrayList<Map<String, Object>>();
			/**SUM(CASE WHEN) is used of counting requests (like issue request, issue cancel request, etc.)
			 *In this IF condition is satisfied than 1 is added to current count ELSE 0 is added to current count
			 *Hence, in the end we will get correct count related to respective request type
			 */
			PreparedStatement ps = con.prepareStatement("SELECT b.book_no, book_title, book_author, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) issue_pending, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) issue_cancelled, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) issue_closed, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) return_pending, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) return_cancelled, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) return_closed  FROM book b INNER JOIN book_request br ON b.book_no=br.book_no INNER JOIN book_sub_request bsr ON br.request_no=bsr.request_no WHERE book_category = ? AND request_date BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY) GROUP BY b.book_no");
			ps.setString(1, b.getBookCategory());
			ps.setString(2, fromDate);
			ps.setString(3, toDate);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap<String, Object>();
				book = new Book();
				book.setBookNo(rs.getInt("book_no"));
				book.setBookTitle(rs.getString("book_title"));
				book.setBookAuthor(rs.getString("book_author"));
				map.put("issuePending", rs.getInt("issue_pending"));
				map.put("issueCancelled", rs.getInt("issue_cancelled"));
				map.put("issueClosed", rs.getInt("issue_closed"));
				map.put("returnPending", rs.getInt("return_pending"));
				map.put("returnCancelled", rs.getInt("return_cancelled"));
				map.put("returnClosed", rs.getInt("return_closed"));
				map.put("book", book);
				booksReportByCategory.add(map);
			}
			
		} finally {
			if (con != null)
				con.close();
		}
		return booksReportByCategory;
	}
	
	/**
	 * Gets the reports counters sum. In this counters (like issue book count, issue cancel count, etc.) of all books are added. 
	 * This counters are used in data visualization on client side.
	 * @param fromDate the date from which report is to be generated
	 * @param toDate the date to which report is to be generated
	 * @return the reports sum
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public Map<String,Object> getReportsSum(String fromDate, String toDate) throws SQLException, Exception{
		logger.info("Getting Report Sum");
		Connection con = null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			/**SUM(CASE WHEN) is used of counting requests (like issue request, issue cancel request, etc.)
			 *In this IF condition is satisfied than 1 is added to current count ELSE 0 is added to current count
			 *Hence, in the end we will get correct count related to respective request type
			 */
			PreparedStatement ps = con.prepareStatement("SELECT SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) issue_pending, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) issue_cancelled, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) issue_closed, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) return_pending, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) return_cancelled, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) return_closed  FROM book b INNER JOIN book_request br ON b.book_no=br.book_no INNER JOIN book_sub_request bsr ON br.request_no=bsr.request_no WHERE request_date BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY)");
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ResultSet rs = ps.executeQuery();
			map=new HashMap<String, Object>();
			if(rs.next()){
				map.put("issuePending", rs.getInt("issue_pending"));
				map.put("issueCancelled", rs.getInt("issue_cancelled"));
				map.put("issueClosed", rs.getInt("issue_closed"));
				map.put("returnPending", rs.getInt("return_pending"));
				map.put("returnCancelled", rs.getInt("return_cancelled"));
				map.put("returnClosed", rs.getInt("return_closed"));
			} else{
				map.put("issuePending", 0);
				map.put("issueCancelled", 0);
				map.put("issueClosed", 0);
				map.put("returnPending", 0);
				map.put("returnCancelled", 0);
				map.put("returnClosed", 0);
			}
		} finally{
			if (con != null)
				con.close();
		}
		return map;
	}
	
	/**
	 * Gets the reports by title sum.
	 *
	 * @param book the book
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the reports by title sum
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public Map<String,Object> getReportsByTitleSum(Book book, String fromDate, String toDate) throws SQLException, Exception{
		logger.info("Getting Report By Title Sum");
		Connection con = null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) issue_pending, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) issue_cancelled, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) issue_closed, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) return_pending, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) return_cancelled, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) return_closed  FROM book b INNER JOIN book_request br ON b.book_no=br.book_no INNER JOIN book_sub_request bsr ON br.request_no=bsr.request_no WHERE book_title LIKE ? AND request_date BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY)");
			ps.setString(1, "%" + book.getBookTitle() + "%");
			ps.setString(2, fromDate);
			ps.setString(3, toDate);
			ResultSet rs = ps.executeQuery();
			map=new HashMap<String, Object>();
			if(rs.next()){
				map.put("issuePending", rs.getInt("issue_pending"));
				map.put("issueCancelled", rs.getInt("issue_cancelled"));
				map.put("issueClosed", rs.getInt("issue_closed"));
				map.put("returnPending", rs.getInt("return_pending"));
				map.put("returnCancelled", rs.getInt("return_cancelled"));
				map.put("returnClosed", rs.getInt("return_closed"));
			} else{
				map.put("issuePending", 0);
				map.put("issueCancelled", 0);
				map.put("issueClosed", 0);
				map.put("returnPending", 0);
				map.put("returnCancelled", 0);
				map.put("returnClosed", 0);
			}
		} finally{
			if (con != null)
				con.close();
		}
		return map;
	}
	
	/**
	 * Gets the reports by author sum.
	 *
	 * @param book the book
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the reports by author sum
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public Map<String,Object> getReportsByAuthorSum(Book book, String fromDate, String toDate) throws SQLException, Exception{
		logger.info("Getting Report By Author Sum");
		Connection con = null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) issue_pending, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) issue_cancelled, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) issue_closed, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) return_pending, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) return_cancelled, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) return_closed  FROM book b INNER JOIN book_request br ON b.book_no=br.book_no INNER JOIN book_sub_request bsr ON br.request_no=bsr.request_no WHERE book_author = ? AND request_date BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY)");
			ps.setString(1, book.getBookAuthor());
			ps.setString(2, fromDate);
			ps.setString(3, toDate);
			ResultSet rs = ps.executeQuery();
			map=new HashMap<String, Object>();
			if(rs.next()){
				map.put("issuePending", rs.getInt("issue_pending"));
				map.put("issueCancelled", rs.getInt("issue_cancelled"));
				map.put("issueClosed", rs.getInt("issue_closed"));
				map.put("returnPending", rs.getInt("return_pending"));
				map.put("returnCancelled", rs.getInt("return_cancelled"));
				map.put("returnClosed", rs.getInt("return_closed"));
			} else{
				map.put("issuePending", 0);
				map.put("issueCancelled", 0);
				map.put("issueClosed", 0);
				map.put("returnPending", 0);
				map.put("returnCancelled", 0);
				map.put("returnClosed", 0);
			}
		} finally{
			if (con != null)
				con.close();
		}
		return map;
	}
	
	/**
	 * Gets the reports by category sum.
	 *
	 * @param book the book
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the reports by category sum
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public Map<String,Object> getReportsByCategorySum(Book book, String fromDate, String toDate) throws SQLException, Exception{
		logger.info("Getting Report By Category Sum");
		Connection con = null;
		Map<String, Object> map=null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) issue_pending, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) issue_cancelled, SUM(CASE WHEN bsr.request_type='DELIVERY' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) issue_closed, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='PENDING' THEN 1 ELSE 0 END) return_pending, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CANCELLED' THEN 1 ELSE 0 END) return_cancelled, SUM(CASE WHEN bsr.request_type='RETURN' AND bsr.request_sub_type='CLOSED' THEN 1 ELSE 0 END) return_closed  FROM book b INNER JOIN book_request br ON b.book_no=br.book_no INNER JOIN book_sub_request bsr ON br.request_no=bsr.request_no WHERE book_category = ? AND request_date BETWEEN ? AND DATE_ADD(?,INTERVAL 1 DAY)");
			ps.setString(1, book.getBookCategory());
			ps.setString(2, fromDate);
			ps.setString(3, toDate);
			ResultSet rs = ps.executeQuery();
			map=new HashMap<String, Object>();
			if(rs.next()){
				map.put("issuePending", rs.getInt("issue_pending"));
				map.put("issueCancelled", rs.getInt("issue_cancelled"));
				map.put("issueClosed", rs.getInt("issue_closed"));
				map.put("returnPending", rs.getInt("return_pending"));
				map.put("returnCancelled", rs.getInt("return_cancelled"));
				map.put("returnClosed", rs.getInt("return_closed"));
			} else{
				map.put("issuePending", 0);
				map.put("issueCancelled", 0);
				map.put("issueClosed", 0);
				map.put("returnPending", 0);
				map.put("returnCancelled", 0);
				map.put("returnClosed", 0);
			}
		} finally{
			if (con != null)
				con.close();
		}
		return map;
	}
	
	/**
	 * Checks whether book is already issued by user or not.
	 *
	 * @param user : user with whose respect issue status is to be checked
	 * @param book : book whose issue status is to be checked
	 * @return the book issue count (0 implies book is not issued by user)
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public int getBookIssueStatus(User user, Book book) throws SQLException, Exception{
		logger.info("Getting Book Issue Status");
		Connection con= null;
		int bookIssuedOrNot=-1;
		try{
			con = conn.getConnection();
			PreparedStatement ps;
			ResultSet rs;
			ps = con.prepareStatement("SELECT COUNT(*) book_already_issued FROM book_request WHERE user_email=? AND book_no=? AND delivery_status!='CANCELLED' AND return_status !='CLOSED'");
			ps.setString(1, user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			rs = ps.executeQuery();
			rs.next();
			bookIssuedOrNot=rs.getInt("book_already_issued");
		} finally{
			if(con!=null)
				con.close();
		}
		return bookIssuedOrNot;
	}
}