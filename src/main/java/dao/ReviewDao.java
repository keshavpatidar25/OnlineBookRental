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
import dto.Rating;
import dto.Review;


/**
 * The Class ReviewDao.
 */
public class ReviewDao {
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(ReviewDao.class);
	
	/** Database connection singleton class object. */
	private MyConnection conn = new MyConnection();
	
	/**
	 * Gets the book reviews.
	 *
	 * @param book the book whose reviews are to be fetched
	 * @return the book reviews
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getBookReviews(Book book) throws SQLException, Exception{
		logger.info("Getting Book Reviews");
		Connection con = null;
		List<Map<String,Object>> reviewList = null;
		Map<String,Object> map = null;
		Review review = null;
		Rating rating = null;
		try{
			con = conn.getConnection();
			reviewList = new ArrayList<Map<String,Object>>();
			PreparedStatement ps = con.prepareStatement("SELECT br.*,br1.rating FROM book_review br LEFT JOIN book_rating br1 ON br.user_email = br1.user_email  AND br.book_no=br1.book_no WHERE br.book_no = ?");
			ps.setInt(1, book.getBookNo());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap<String, Object>();
				review = new Review();
				rating = new Rating();
				review.setUserEmail(rs.getString("user_email"));
				review.setBookReview(rs.getString("review"));
				review.setReviewDate(rs.getTimestamp("review_date"));
				rating.setBookRating(rs.getInt("rating"));
				map.put("review", review);
				map.put("rating", rating);
				reviewList.add(map);
			}
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return reviewList;
	}
	
	/**
	 * Adds the review.
	 *
	 * @param review the review class object containing book review, user who has reviewed and the book to be reviewed.
	 * @return the status of the add review operation whether success or failure.
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String addReview(Review review) throws SQLException, Exception{
		logger.info("Adding Book Review");
		String result=null;
		Connection con = null;
		try{
			con=conn.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO book_review(user_email,book_no,review,review_date) VALUES(?,?,?,?)");
			ps.setString(1, review.getUserEmail());
			ps.setInt(2, review.getBookNo());
			ps.setString(3, review.getBookReview());
			ps.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
			ps.executeUpdate();
			result="SUCCESS";
		} catch(SQLException e){
			result="ERROR : Database Error.";
		} catch(Exception e){
			result="ERROR : Server Error.";
		} finally{
			if(con!=null){
				con.close();
			}
		}
		return result;
	}
}
