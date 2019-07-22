package dto;


/**
 * The Class Request.
 */
public class Request {
	
	/** The request no, book no. */
	private int requestNo,bookNo;
	
	/** The user email, delivery status and return status. */
	private String userEmail, deliveryStatus, returnStatus;
	
	/**
	 * Instantiates a new request.
	 */
	public Request() {
	
	}

	/**
	 * Gets the request no.
	 *
	 * @return the request no
	 */
	public int getRequestNo() {
		return requestNo;
	}


	/**
	 * Sets the request no.
	 *
	 * @param requestNo the new request no
	 */
	public void setRequestNo(int requestNo) {
		this.requestNo = requestNo;
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
	 * Gets the delivery status.
	 *
	 * @return the delivery status
	 */
	public String getDeliveryStatus() {
		return deliveryStatus;
	}


	/**
	 * Sets the delivery status.
	 *
	 * @param deliveryStatus the new delivery status
	 */
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}


	/**
	 * Gets the return status.
	 *
	 * @return the return status
	 */
	public String getReturnStatus() {
		return returnStatus;
	}


	/**
	 * Sets the return status.
	 *
	 * @param returnStatus the new return status
	 */
	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	/** 
	 * Request Class to String method
	 */
	@Override
	public String toString() {
		return "Request [requestNo=" + requestNo + ", bookNo=" + bookNo
				+ ", userEmail=" + userEmail + ", deliveryStatus="
				+ deliveryStatus + ", returnStatus=" + returnStatus + "]";
	}
}
