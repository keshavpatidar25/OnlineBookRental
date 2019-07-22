package dto;

/**
 * The Class Rating.
 */
public class Rating {
	
	/** book Rating, book no */
	private int bookRating, bookNo;
	
	/** user email */
	private String userEmail;
	
	/**
	 * Instantiates a new rating.
	 */
	public Rating() {
	}

	/**
	 * Gets the book rating.
	 *
	 * @return the book rating
	 */
	public int getBookRating() {
		return bookRating;
	}

	/**
	 * Sets the book rating.
	 *
	 * @param bookRating the new book rating
	 */
	public void setBookRating(int bookRating) {
		this.bookRating = bookRating;
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
	 * Rating Class to String method
	 */
	@Override
	public String toString() {
		return "Rating [bookRating=" + bookRating + ", bookNo=" + bookNo
				+ ", userEmail=" + userEmail + "]";
	}
	
	
}
