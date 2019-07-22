package dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import conn.MyConnection;
import dto.Book;
import dto.Request;
import dto.User;

/**
 * The Class BookDao.
 */
public class BookDao {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(BookDao.class);
	
	/** Database connection singleton class object. */
	private MyConnection conn = new MyConnection();
	
	/**
	 * Gets the book categories.
	 *
	 * @return the book categories
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<String> getBookCategories() throws SQLException,Exception{
		logger.info("Getting Book Categories");
		Connection con= null;
		List<String> categoryList=null;
		try{
			con = conn.getConnection();
			categoryList = new ArrayList<String>();
			PreparedStatement ps = con.prepareStatement("SELECT DISTINCT book_category FROM BOOK ORDER BY book_category");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				categoryList.add(rs.getString("book_category"));
			
		} finally{
			if(con!=null)
				con.close();
		}
		return categoryList;
	}
	
	/**
	 * Gets the book authors.
	 *
	 * @return the book authors
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<String> getBookAuthors() throws SQLException,Exception{
		logger.info("Getting Book Authors");
		Connection con= null;
		List<String> authorList=null;
		try{
			con = conn.getConnection();
			authorList = new ArrayList<String>();
			PreparedStatement ps = con.prepareStatement("SELECT DISTINCT book_author FROM BOOK ORDER BY book_author");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				authorList.add(rs.getString("book_author"));
			
		} finally{
			if(con!=null)
				con.close();
		}
		return authorList;
	}
	
	/**
	 * Gets the book availablity.
	 *
	 * @param book : book whose availability is to be found
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public void getBookAvailablity(Book book) throws SQLException, Exception{
		logger.info("Getting Book Availability");
		Connection con= null;
		try{
			con = new MyConnection().getConnection();
			PreparedStatement ps;
			ResultSet rs;
			/** Book availability is calculated by subtracting book quantity with no. of books issued */
			ps = con.prepareStatement("SELECT book_qty-COUNT(br.book_no) book_available FROM book b LEFT JOIN book_request br ON b.book_no=br.book_no AND delivery_status!='CANCELLED' AND return_status!='CLOSED' WHERE b.book_no=?");
			ps.setInt(1, book.getBookNo());
			rs = ps.executeQuery();
			if(rs.next()){
				book.setBookAvailable(rs.getInt("book_available"));
			} else{
				book.setBookAvailable(book.getBookQty());
			}
			
		} finally{
			if(con!=null)
				con.close();
		}
	}
	
	/**
	 * Gets the book issued count.
	 *
	 * @param book : the book whose issue count is to be found
	 * @return the book issued count
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public int getBookIssuedCount(Book book) throws SQLException, Exception{
		logger.info("Getting Book Issued Count");
		Connection con= null;
		int bookIssueCount=0;
		try{
			con = new MyConnection().getConnection();
			PreparedStatement ps;
			ResultSet rs;
			/**This gives no of times book has been issued since past month. This logic is used in sorting books on basis of most popular.*/
			ps = con.prepareStatement("SELECT COUNT(*) issue_count  FROM book_request br INNER JOIN book_sub_request br1 ON br.request_no=br1.request_no WHERE request_type='DELIVERY' AND request_sub_type='PENDING' AND request_date>DATE_SUB(NOW(),INTERVAL 1 MONTH) AND book_no=?");
			ps.setInt(1, book.getBookNo());
			rs = ps.executeQuery();
			rs.next();
			bookIssueCount=rs.getInt("issue_count");
		} finally{
			if(con!=null)
				con.close();
		}
		return bookIssueCount;
	}
	
	/**
	 * Gets the books list by all.
	 *
	 * @param filter : the string by which books are to be filtered
	 * @return the books list by all
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String, Object>> getBooksListByAll(String filter) throws SQLException,Exception{
		logger.info("Getting Books List By All");
		List<Map<String,Object>> booksList = null;
		Connection con = null;
		Map<String, Object> map = null, ratingMap = null;
		Book book = null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Map<String,Object>>();
			/** Why Concat is Used?
			 *  As user can enter any combination of book category, book author or book title. So, by concatinating we can achieve this.  
			 */
			PreparedStatement ps = con.prepareStatement("SELECT book_no FROM book WHERE CONCAT(book_category,' ',book_title,' ',book_author,' ',book_category,' ',book_title,' ',book_category,' ',book_author,' ',book_title,' ',book_category) LIKE ?");
			ps.setString(1, "%"+filter+"%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book = new Book();
				map = new HashMap<String, Object>();
				ratingMap = new HashMap<String, Object>();
				book.setBookNo(rs.getInt("book_no"));	
				book = this.getBookDetails(book);				
				this.getBookAvailablity(book);
				int bookIssueCount=this.getBookIssuedCount(book);
				ratingMap = new RatingDao().getRatingDetails(book);
				
				map.put("bookDetails", book);
				map.put("bookIssueCount", bookIssueCount);
				map.put("bookRatingDetails", ratingMap);
				booksList.add(map);
			}
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return booksList;
	}
	
	/**
	 * Gets the books list by title.
	 *
	 * @param filter : the string by which books are to be filtered
	 * @return the books list by title
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String, Object>> getBooksListByTitle(String filter) throws SQLException,Exception{
		logger.info("Getting Books List By Title");
		List<Map<String,Object>> booksList = null;
		Connection con = null;
		Map<String, Object> map = null, ratingMap = null;
		Book book = null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Map<String,Object>>();
			PreparedStatement ps = con.prepareStatement("SELECT book_no FROM book WHERE book_title LIKE ?");
			ps.setString(1, "%"+filter+"%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book = new Book();
				map = new HashMap<String, Object>();
				ratingMap = new HashMap<String, Object>();
				book.setBookNo(rs.getInt("book_no"));	
				book = this.getBookDetails(book);				
				this.getBookAvailablity(book);
				int bookIssueCount=this.getBookIssuedCount(book);
				ratingMap = new RatingDao().getRatingDetails(book);
				
				map.put("bookDetails", book);
				map.put("bookIssueCount", bookIssueCount);
				map.put("bookRatingDetails", ratingMap);
				booksList.add(map);
			}
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return booksList;
	}
	
	/**
	 * Gets the books list by author.
	 *
	 * @param filter : the string by which books are to be filtered
	 * @return the books list by author
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String, Object>> getBooksListByAuthor(String filter) throws SQLException,Exception{
		logger.info("Getting Books List By Author");
		List<Map<String,Object>> booksList = null;
		Connection con = null;
		Map<String, Object> map = null, ratingMap = null;
		Book book = null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Map<String,Object>>();
			PreparedStatement ps = con.prepareStatement("SELECT book_no FROM book WHERE book_author LIKE ?");
			ps.setString(1, "%"+filter+"%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book = new Book();
				map = new HashMap<String, Object>();
				ratingMap = new HashMap<String, Object>();
				book.setBookNo(rs.getInt("book_no"));	
				book = this.getBookDetails(book);				
				this.getBookAvailablity(book);
				int bookIssueCount=this.getBookIssuedCount(book);
				ratingMap = new RatingDao().getRatingDetails(book);
				
				map.put("bookDetails", book);
				map.put("bookIssueCount", bookIssueCount);
				map.put("bookRatingDetails", ratingMap);
				booksList.add(map);
			}
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return booksList;
	}
	
	/**
	 * Gets the books list by category.
	 *
	 * @param filter : the string by which books are to be filtered
	 * @return the books list by category
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String, Object>> getBooksListByCategory(String filter) throws SQLException,Exception{
		logger.info("Getting Books List By Category");
		List<Map<String,Object>> booksList = null;
		Connection con = null;
		Map<String, Object> map = null, ratingMap = null;
		Book book = null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Map<String,Object>>();
			PreparedStatement ps = con.prepareStatement("SELECT book_no FROM book WHERE book_category LIKE ?");
			ps.setString(1, "%"+filter+"%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book = new Book();
				map = new HashMap<String, Object>();
				ratingMap = new HashMap<String, Object>();
				book.setBookNo(rs.getInt("book_no"));	
				book = this.getBookDetails(book);				
				this.getBookAvailablity(book);
				int bookIssueCount=this.getBookIssuedCount(book);
				ratingMap = new RatingDao().getRatingDetails(book);
				
				map.put("bookDetails", book);
				map.put("bookIssueCount", bookIssueCount);
				map.put("bookRatingDetails", ratingMap);
				booksList.add(map);
			}
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return booksList;
	}
	
	/**
	 * Gets the books list.
	 *
	 * @return the books list
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Book> getBooksList() throws SQLException,Exception{
		logger.info("Getting All Books List");
		Connection con= null;
		List<Book> booksList=null;
		Book book=null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Book>();
			PreparedStatement ps = con.prepareStatement("SELECT b.book_no, book_qty-COUNT(br.book_no) book_available FROM book b INNER JOIN book_request br ON b.book_no=br.book_no WHERE delivery_status!='CANCELLED' AND return_status!='CLOSED' GROUP BY br.book_no UNION SELECT *,book_qty AS book_available FROM book b WHERE book_no NOT IN (SELECT DISTINCT b.book_no FROM book b LEFT JOIN book_request br ON b.book_no=br.book_no  WHERE delivery_status!='CANCELLED' AND return_status!='CLOSED')");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book=new Book();
				book.setBookNo(rs.getInt("book_no"));	
				book = this.getBookDetails(book);
				book.setBookAvailable(rs.getInt("book_available"));				
				booksList.add(book);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return booksList;
	}
	
	/**
	 * Gets the newly arrived books list.
	 *
	 * @return the newly arrived books list
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Book> getNewlyArrivedBooksList() throws SQLException,Exception{
		logger.info("Getting Newly Arrived Books List");
		Connection con= null;
		List<Book> booksList=null;
		Book book=null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Book>();
			PreparedStatement ps = con.prepareStatement("SELECT book_no FROM book ORDER BY book_arrival DESC, book_no DESC LIMIT 8");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book=new Book();
				book.setBookNo(rs.getInt("book_no"));	
				book=this.getBookDetails(book);
				booksList.add(book);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return booksList;
	}
	
	/**
	 * Gets the bookmarked books.
	 *
	 * @param user : the user whose bookmarked books is to be found
	 * @return the bookmarked books of particular user
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Book> getBookmarkedBooks(User user) throws SQLException, Exception{
		logger.info("Getting Bookmarked Books List");
		Connection con= null;
		List<Book> booksList=null;
		Book book=null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Book>();
			PreparedStatement ps = con.prepareStatement("SELECT book_no FROM user_bookmark WHERE user_email = ?");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book=new Book();
				book.setBookNo(rs.getInt("book_no"));	
				booksList.add(book);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return booksList;
	}
	
	/**
	 * Gets the bookmarked books details.
	 *
	 * @param user the user whose bookmarked books details is to be found
	 * @return the bookmarked books details of particular user
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Book> getBookmarkedBooksDetails(User user) throws SQLException, Exception{
		logger.info("Getting Bookmarked Books Details");
		Connection con= null;
		List<Book> booksList=null;
		Book book=null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Book>();
			PreparedStatement ps = con.prepareStatement("SELECT book_no FROM user_bookmark WHERE user_email = ?");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book=new Book();
				book.setBookNo(rs.getInt("book_no"));	
				book = this.getBookDetails(book);
				this.getBookAvailablity(book);
				booksList.add(book);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return booksList;
	}
	
	/**
	 * Adds the bookmark.
	 *
	 * @param user : the user who has bookmarked the book
	 * @param book : the book which is to be bookmarked
	 * @return the result of the request
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String addBookmark(User user, Book book) throws SQLException,Exception{
		logger.info("Adding Bookmark");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO user_bookmark values(?,?)");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			ps.executeUpdate();
			result="SUCCESS";
		} catch(SQLException e){
			result="ERROR : Database Error.";
		} catch(Exception e){
			result="ERROR : Server Error.";
		} finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Removes the bookmark.
	 *
	 * @param user : the user which has removed the bookmark
	 * @param book : the book which is be unbookmaked
	 * @return the result of the request
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String removeBookmark(User user, Book book) throws SQLException,Exception{
		logger.info("Removing Bookmark");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("DELETE FROM user_bookmark WHERE user_email = ? AND book_no=?");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
			ps.executeUpdate();
			result="SUCCESS";
		} catch(SQLException e){
			result="ERROR : Database Error.";
		} catch(Exception e){
			result="ERROR : Server Error.";
		} finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Gets the issued books.
	 *
	 * @param user : the user whose issued books is to be found
	 * @return the issued books
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getIssuedBooks(User user) throws SQLException, Exception{
		logger.info("Getting Issued Books");
		Connection con= null;
		List<Map<String,Object>> issuedBooksList = new ArrayList<Map<String, Object>>();
		Map<String,Object> issuedBooks=null;
		Book book=null;
		Request request=null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT b.book_no, request_no, delivery_status, return_status FROM book b INNER JOIN book_request br ON b.book_no = br.book_no WHERE user_email=? AND delivery_status!='CANCELLED' AND return_status !='CLOSED';");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book=new Book();
				request = new Request();
				issuedBooks = new HashMap<String,Object>();
				book.setBookNo(rs.getInt("book_no"));
				request.setRequestNo(rs.getInt("request_no"));
				request.setDeliveryStatus(rs.getString("delivery_status"));
				request.setReturnStatus(rs.getString("return_status"));
				issuedBooks.put("book", book);
				issuedBooks.put("request", request);
				issuedBooksList.add(issuedBooks);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return issuedBooksList;
	}
	
	/**
	 * Gets the issued books details.
	 *
	 * @param user : the user whose issued books details is tob be found
	 * @return the issued books details
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getIssuedBooksDetails(User user) throws SQLException, Exception{
		logger.info("Getting Issued Books Details");
		Connection con= null;
		List<Map<String,Object>> issuedBooksList = new ArrayList<Map<String, Object>>();
		Map<String,Object> issuedBooks=null;
		Book book=null;
		Request request=null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT b.book_no, request_no, delivery_status, return_status FROM book b INNER JOIN book_request br ON b.book_no = br.book_no WHERE user_email=? AND delivery_status!='CANCELLED' AND return_status !='CLOSED';");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book=new Book();
				request = new Request();
				issuedBooks = new HashMap<String,Object>();
				book.setBookNo(rs.getInt("book_no"));
				this.getBookDetails(book);
				request.setRequestNo(rs.getInt("request_no"));
				request.setDeliveryStatus(rs.getString("delivery_status"));
				request.setReturnStatus(rs.getString("return_status"));
				issuedBooks.put("book", book);
				issuedBooks.put("request", request);
				issuedBooksList.add(issuedBooks);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return issuedBooksList;
	}

	/**
	 * Gets the book details.
	 *
	 * @param book : the book whose details is to be found
	 * @return the book details
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public Book getBookDetails(Book book) throws SQLException,Exception{
		logger.info("Getting Book Details");
		Connection con= null;
		try{
			con = new MyConnection().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM book where book_no=?");
			ps.setInt(1, book.getBookNo());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){	
				book.setBookTitle(rs.getString("book_title"));
				book.setBookAuthor(rs.getString("book_author"));
				book.setBookCategory(rs.getString("book_category"));	
				book.setBookPublisher(rs.getString("book_publisher"));
				book.setBookDescription(rs.getString("book_desc"));
				book.setBookArrival(rs.getDate("book_arrival"));
				book.setBookQty(rs.getInt("book_qty"));
				book.setBookPrice(rs.getFloat("book_price"));
				Blob bookBlob = rs.getBlob("book_cover");
				/**bookBlob.getBytes(start index, length) will convert blob to byte[] starting from start index till length from start index*/
				book.setBookCover(bookBlob.getBytes(1,(int)bookBlob.length()));
				bookBlob.free();		//Free memory
			} else{
				book=null;
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return book;
	}
	
	/**
	 * Gets the most popular books list.
	 *
	 * @return the most popular books list
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Book> getMostPopularBooksList() throws SQLException,Exception{
		logger.info("Getting Most Popular Books List");
		Connection con= null;
		List<Book> booksList=null;
		Book book=null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Book>();
			/** Most Popular books are found on basis of three factors
			 * (i)Issue Book Count (First Priority)
			 * (ii)Rating of Book (Second Priority) 
			 * (iii)No. of Users who have rated the book (Last Priority)
			 * Limit 8
			 */
			PreparedStatement ps = con.prepareStatement("SELECT br1.book_no FROM book_rating br RIGHT JOIN book_request br1 ON br.book_no=br1.book_no INNER JOIN book_sub_request bsr1 ON br1.request_no=bsr1.request_no WHERE request_type='DELIVERY' AND request_sub_type='PENDING' AND request_date>DATE_SUB(NOW(),INTERVAL 1 MONTH) GROUP BY br1.book_no ORDER BY COUNT(br1.book_no) DESC,br.rating DESC, COUNT(br.book_no) DESC LIMIT 8");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book=new Book();
				book.setBookNo(rs.getInt("book_no"));
				
				book = this.getBookDetails(book);
				booksList.add(book);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return booksList;
	}
	
	/**
	 * Gets the recommended books list.
	 *
	 * @param user the user to whom books are to be recommended
	 * @return the recommended books list
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Book> getRecommendedBooksList(User user) throws SQLException, Exception{
		logger.info("Getting Recommended Books List");
		Connection con= null;
		List<Book> booksList=null;
		Book book=null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Book>();
			/**Recommended Books are fetched in three steps:
			 * (i) Selects the books with same author and same category as the books which were issued by user. Ordered by Issue Count Decreasing. Limit it to 8.
			 * Moreover, those books are not selected which were already issued by user.
			 * (ii) Selects the books with same author as the books which were issued by user. Ordered by Issue Count Decreasing.
			 * Limit it to 8 including books selected in Step (i) 
			 * (iii)Selects the books with same category as the books which were issued by user. Ordered by Issue Count Decreasing.
			 * Limit it to 8 including books selected in Step (i) & Step (ii)
			 */
			PreparedStatement ps = con.prepareStatement("SELECT book_no FROM BOOK WHERE book_author IN (SELECT DISTINCT book_author FROM book b INNER JOIN book_request br ON b.book_no=br.book_no WHERE user_email=? GROUP BY b.book_no ORDER BY COUNT(b.book_no) DESC) AND book_category IN (SELECT DISTINCT book_category FROM book b INNER JOIN book_request br ON b.book_no=br.book_no WHERE user_email=? GROUP BY b.book_no ORDER BY COUNT(b.book_no) DESC) AND book_no NOT IN(SELECT DISTINCT book_no FROM book_request WHERE user_email=?) UNION SELECT book_no FROM BOOK WHERE book_author IN (SELECT DISTINCT book_author FROM book b INNER JOIN book_request br ON b.book_no=br.book_no WHERE user_email=? GROUP BY b.book_no ORDER BY COUNT(b.book_no) DESC) AND book_no NOT IN(SELECT DISTINCT book_no FROM book_request WHERE user_email=?) UNION SELECT book_no FROM BOOK WHERE book_category IN (SELECT DISTINCT book_category FROM book b INNER JOIN book_request br ON b.book_no=br.book_no WHERE user_email=? GROUP BY b.book_no ORDER BY COUNT(b.book_no) DESC) AND book_no NOT IN(SELECT DISTINCT book_no FROM book_request WHERE user_email=?) LIMIT 8");
			ps.setString(1, user.getUserEmail());
			ps.setString(2, user.getUserEmail());
			ps.setString(3, user.getUserEmail());
			ps.setString(4, user.getUserEmail());
			ps.setString(5, user.getUserEmail());
			ps.setString(6, user.getUserEmail());
			ps.setString(7, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book=new Book();
				book.setBookNo(rs.getInt("book_no"));
				booksList.add(book);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return booksList;
	}
	
	/**
	 * Adds recommended book.
	 *
	 * @param user : the user to whom the book is to be recommended
	 * @param book : the book which is to be recommended
	 * @return the result of the request
	 * @throws SQLException the SQL exception
	 */
	public String addRecommendedBook(User user, Book book) throws SQLException{
		logger.info("Adding Recommended Book");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO user_book_recommended values(?,?)");
			ps.setString(1,user.getUserEmail());
			ps.setInt(2, book.getBookNo());
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
	 * Remove(Truncate) recommended books.
	 *
	 * @return the result of the request
	 * @throws SQLException the SQL exception
	 */
	public String removeRecommendedBooks() throws SQLException{
		logger.info("Removing Recommended Book");
		Connection con= null;
		String result=null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("TRUNCATE TABLE user_book_recommended");
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
	 * Gets the recommended books details list.
	 *
	 * @param user the user to whom books are to be recommended
	 * @return the recommended books list with book details
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Book> getRecommendedBooksDetailsList(User user) throws SQLException, Exception{
		logger.info("Getting Recommended Books Details");
		Connection con= null;
		List<Book> booksList=null;
		Book book=null;
		try{
			con = conn.getConnection();
			booksList = new ArrayList<Book>();
			PreparedStatement ps = con.prepareStatement("SELECT book_no FROM user_book_recommended WHERE user_email=?");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				book=new Book();
				book.setBookNo(rs.getInt("book_no"));
				book=this.getBookDetails(book);
				booksList.add(book);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return booksList;
	}
	
	
	/**
	 * Adds the new book.
	 *
	 * @param book : book to be added
	 * @return : result string
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String addNewBook(Book book) throws SQLException, Exception{
		logger.info("Adding New Book");
		Connection con = null;
		String result=null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO book(book_title,book_author,book_category,book_publisher,book_cover,book_price,book_qty,book_desc,book_arrival) VALUES(?,?,?,?,?,?,?,?,NOW())");
			
			ps.setString(1, book.getBookTitle());
			ps.setString(2, book.getBookAuthor());
			ps.setString(3, book.getBookCategory());
			ps.setString(4, book.getBookPublisher());
			/** javax.sql.rowswet.serial.SerialBlob(byte[]) : Blob is used to convert byte[] to Blob */
			ps.setBlob(5, new javax.sql.rowset.serial.SerialBlob(book.getBookCover()));
			ps.setFloat(6, book.getBookPrice());
			ps.setInt(7, book.getBookQty());
			ps.setString(8, book.getBookDescription());
			ps.executeUpdate();
			result="SUCCESS";
		} finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Update book.
	 *
	 * @param book the book to be updated
	 * @return the result string
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String updateBook(Book book) throws SQLException, Exception{
		logger.info("Updating Book");
		Connection con = null;
		String result=null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE book SET book_title=?,book_author=?,book_category=?,book_publisher=?,book_cover=?,book_price=?,book_qty=?,book_desc=? WHERE book_no=?");
			ps.setString(1, book.getBookTitle());
			ps.setString(2, book.getBookAuthor());
			ps.setString(3, book.getBookCategory());
			ps.setString(4, book.getBookPublisher());
			/**javax.sql.rowswet.serial.SerialBlob(byte[]) : Blob is used to convert byte[] to Blob*/
			ps.setBlob(5, new javax.sql.rowset.serial.SerialBlob(book.getBookCover()));
			ps.setFloat(6, book.getBookPrice());
			ps.setInt(7, book.getBookQty());
			ps.setString(8, book.getBookDescription());
			ps.setInt(9, book.getBookNo());
			ps.executeUpdate();
			result="SUCCESS";
		} finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Delete book.
	 *
	 * @param book the book to be deleted
	 * @return the result string
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String deleteBook(Book book) throws SQLException, Exception{
		logger.info("Deleting Book");
		Connection con = null;
		String result=null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("DELETE FROM book WHERE book_no = ?");
			ps.setInt(1, book.getBookNo());
			ps.executeUpdate();
			result="SUCCESS";
		} finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
}
