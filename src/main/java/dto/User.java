package dto;


/**
 * The Class User.
 */
public class User {
	
	/** The user email, user password, user name, user address, user contact, user role. */
	private String userEmail, userPass, userName, userAddress, userContact, userRole;
	
	/**
	 * Instantiates a new user.
	 */
	public User() {
		
	}
	
	/**
	 * Instantiates a new user.
	 *
	 * @param userEmail the user email
	 */
	public User(String userEmail) {
		super();
		this.userEmail = userEmail;
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
	 * Gets the user pass.
	 *
	 * @return the user pass
	 */
	public String getUserPass() {
		return userPass;
	}

	/**
	 * Sets the user pass.
	 *
	 * @param userPass the new user pass
	 */
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the user address.
	 *
	 * @return the user address
	 */
	public String getUserAddress() {
		return userAddress;
	}

	/**
	 * Sets the user address.
	 *
	 * @param userAddress the new user address
	 */
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	/**
	 * Gets the user role.
	 *
	 * @return the user role
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * Sets the user role.
	 *
	 * @param userRole the new user role
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * Gets the user contact.
	 *
	 * @return the user contact
	 */
	public String getUserContact() {
		return userContact;
	}

	/**
	 * Sets the user contact.
	 *
	 * @param userContact the new user contact
	 */
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}


	/**
	 * User Class to String method
	 */
	@Override
	public String toString() {
		return "User [userEmail=" + userEmail + ", userName=" + userName
				+ ", userAddress=" + userAddress + ", userRole=" + userRole
				+ ", userContact=" + userContact + "]";
	}
	
	
}
