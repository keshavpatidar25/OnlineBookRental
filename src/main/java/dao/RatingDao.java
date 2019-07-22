package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import conn.MyConnection;
import dto.Book;
import dto.Rating;
import dto.User;

/**
 * The Class RatingDao.
 */
public class RatingDao {

	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(RatingDao.class);
			
	/** Database connection singleton class object. */
	private MyConnection conn = new MyConnection();
	
	/**
	 * Gets the rating details(average rating of all users, count of users who have rated) of particular book.
	 *
	 * @param book the book whose rating details are to be found
	 * @return the rating details (average rating of the book and no of users who have rated for the book)
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public Map<String, Object> getRatingDetails(Book book) throws SQLException,Exception{
		logger.info("Getting Rating Details");
		Connection con = null;
		Map<String, Object> map = null;
		try{
			con = conn.getConnection();
			map = new HashMap<String, Object>();
			PreparedStatement ps = con.prepareStatement("SELECT IFNULL(AVG(rating),0) AS rating, COUNT(*) AS count FROM book_rating WHERE book_no=?");
			ps.setInt(1, book.getBookNo());
			ResultSet rs = ps.executeQuery();
			rs.next();
			map.put("avgRating", rs.getFloat("rating"));
			map.put("ratingCount", rs.getInt("count"));
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return map;
	}
	
	/**
	 * Gets the book rating of particular user.
	 *
	 * @param user the user who has rated the book
	 * @param book the book whose rating is to be found
	 * @return the book rating
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public Rating getBookRating(User user,Book book) throws SQLException, Exception{
		logger.info("Getting Book Rating");
		Connection con = null;
		Rating rating = null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT rating FROM book_rating WHERE book_no = ? AND user_email = ?");
			ps.setInt(1, book.getBookNo());
			ps.setString(2, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			rating = new  Rating();
			if(rs.next()){
				rating.setBookRating(rs.getInt("rating"));
			} else{
				rating.setBookRating(0);
			}
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return rating;
	}
	
	/**
	 * Checks for user rated.
	 *
	 * @param rating the rating
	 * @return true, if user has rated
	 * @return false if user has not rated
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	protected boolean hasUserRated(Rating rating) throws SQLException, Exception{
		logger.info("Checking has user rated");
		boolean result = false;
		Connection con = null;
		try{
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM book_rating WHERE user_email = ? AND book_no = ?");
			ps.setString(1, rating.getUserEmail());
			ps.setInt(2, rating.getBookNo());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				result=true;
			}
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return result;
	}
	
	/**
	 * Adds the rating.
	 *
	 * @param rating the rating of the book
	 * @return the result whether add rating is successful or failure
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	protected String addRating(Rating rating) throws SQLException, Exception{
		logger.info("Add Book Rating");
		String result=null;
		Connection con = null;
		try{
			con=conn.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO book_rating VALUES(?,?,?)");
			ps.setString(1, rating.getUserEmail());
			ps.setInt(2, rating.getBookNo());
			ps.setInt(3, rating.getBookRating());
			ps.executeUpdate();
			result="SUCCESS";
		} catch(SQLException e){
			result="ERROR : Database Error.";
			logger.error("SQL Exception Occured while adding book rating", e);
		} catch(Exception e){			
			result="ERROR : Server Error.";
			logger.error("Exception Occured while adding book rating", e);
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return result;
	}
	
	/**
	 * Update rating.
	 *
	 * @param rating the rating of the book
	 * @return the result whether add rating is successful or failure
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	protected String updateRating(Rating rating) throws SQLException, Exception{
		logger.info("Update Book Rating");
		String result=null;
		Connection con = null;
		try{
			con=conn.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE book_rating SET rating=? WHERE user_email=? AND book_no=?");
			ps.setInt(1, rating.getBookRating());
			ps.setString(2, rating.getUserEmail());
			ps.setInt(3, rating.getBookNo());
			ps.executeUpdate();
			result="SUCCESS";
		} catch(SQLException e){
			result="ERROR : Database Error.";
			logger.error("SQL Exception Occured while updating book rating", e);
		} catch(Exception e){
			result="ERROR : Server Error.";
			logger.error("Exception Occured while updating book rating", e);
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return result;
	}
	
	/**
	 * Adds the or update rating.
	 *
	 * @param rating the rating of the book
	 * @return the result whether rating operation is successful or failure
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String addOrUpdateRating(Rating rating) throws SQLException, Exception{
		logger.info("Add or Update Rating");
		String result = null;
		/**if user has already rated for the book then update rating else add rating*/
		if(hasUserRated(rating)) {			
			result = updateRating(rating);
		} else {
			result = addRating(rating);
		}
		return result;
	}
}
