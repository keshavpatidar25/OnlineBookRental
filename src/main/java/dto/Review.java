package dto;

import java.sql.Timestamp;

/**
 * The Class Review.
 */
public class Review {
	
	/** The book review no, book no. */
	private int bookReviewNo,bookNo;
	
	/** The book review. */
	private String userEmail, bookReview;
	
	/** The review date. */
	private Timestamp reviewDate;
	
	/**
	 * Instantiates a new review.
	 */
	public Review() {
	}
	
	/**
	 * Gets the book review no.
	 *
	 * @return the book review no
	 */
	public int getBookReviewNo() {
		return bookReviewNo;
	}
	
	/**
	 * Sets the book review no.
	 *
	 * @param bookReviewNo the new book review no
	 */
	public void setBookReviewNo(int bookReviewNo) {
		this.bookReviewNo = bookReviewNo;
	}
	
	/**
	 * Gets the book no.
	 *
	 * @return the book no
	 */
	public int getBookNo() {
		return bookNo;
	}
	
	/**
	 * Sets the book no.
	 *
	 * @param bookNo the new book no
	 */
	public void setBookNo(int bookNo) {
		this.bookNo = bookNo;
	}
	
	/**
	 * Gets the user email.
	 *
	 * @return the user email
	 */
	public String getUserEmail() {
		return userEmail;
	}
	
	/**
	 * Sets the user email.
	 *
	 * @param userEmail the new user email
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	/**
	 * Gets the book review.
	 *
	 * @return the book review
	 */
	public String getBookReview() {
		return bookReview;
	}
	
	/**
	 * Sets the book review.
	 *
	 * @param bookReview the new book review
	 */
	public void setBookReview(String bookReview) {
		this.bookReview = bookReview;
	}
	
	/**
	 * Gets the review date.
	 *
	 * @return the review date
	 */
	public Timestamp getReviewDate() {
		return reviewDate;
	}
	
	/**
	 * Sets the review date.
	 *
	 * @param reviewDate the new review date
	 */
	public void setReviewDate(Timestamp reviewDate) {
		this.reviewDate = reviewDate;
	}
	
	/**
	 * Review Class to String method
	 */
	@Override
	public String toString() {
		return "Review [bookReviewNo=" + bookReviewNo + ", bookNo=" + bookNo
				+ ", userEmail=" + userEmail + ", bookReview=" + bookReview
				+ ", reviewDate=" + reviewDate + "]";
	}
}
