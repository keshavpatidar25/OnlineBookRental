package dto;


import java.sql.Timestamp;


/**
 * The Class SubRequest.
 */
public class SubRequest {
	
	/** The sub request no. */
	private int subRequestNo;
	
	/** The sub request date. */
	private Timestamp subRequestDate;
	
	/** The sub request type, sub request sub type, sub request address, sub request contact no., sub request by. */
	private String subRequestType, subRequestSubType, subRequestAddress, subRequestContact, subRequestBy;
	
	/**
	 * Instantiates a new sub request.
	 */
	public SubRequest() {
		
	}
	
	/**
	 * Gets the sub request no.
	 *
	 * @return the sub request no
	 */
	public int getSubRequestNo() {
		return subRequestNo;
	}
	
	/**
	 * Sets the sub request no.
	 *
	 * @param subRequestNo the new sub request no
	 */
	public void setSubRequestNo(int subRequestNo) {
		this.subRequestNo = subRequestNo;
	}
	
	/**
	 * Gets the sub request date.
	 *
	 * @return the sub request date
	 */
	public Timestamp getSubRequestDate() {
		return subRequestDate;
	}


	/**
	 * Sets the sub request date.
	 *
	 * @param subRequestDate the new sub request date
	 */
	public void setSubRequestDate(Timestamp subRequestDate) {
		this.subRequestDate = subRequestDate;
	}


	/**
	 * Gets the sub request type.
	 *
	 * @return the sub request type
	 */
	public String getSubRequestType() {
		return subRequestType;
	}
	
	/**
	 * Sets the sub request type.
	 *
	 * @param subRequestType the new sub request type
	 */
	public void setSubRequestType(String subRequestType) {
		this.subRequestType = subRequestType;
	}
	
	/**
	 * Gets the sub request sub type.
	 *
	 * @return the sub request sub type
	 */
	public String getSubRequestSubType() {
		return subRequestSubType;
	}
	
	/**
	 * Sets the sub request sub type.
	 *
	 * @param subRequestSubType the new sub request sub type
	 */
	public void setSubRequestSubType(String subRequestSubType) {
		this.subRequestSubType = subRequestSubType;
	}
	
	/**
	 * Gets the sub request address.
	 *
	 * @return the sub request address
	 */
	public String getSubRequestAddress() {
		return subRequestAddress;
	}
	
	/**
	 * Sets the sub request address.
	 *
	 * @param subRequestAddress the new sub request address
	 */
	public void setSubRequestAddress(String subRequestAddress) {
		this.subRequestAddress = subRequestAddress;
	}
	
	/**
	 * Gets the sub request contact.
	 *
	 * @return the sub request contact
	 */
	public String getSubRequestContact() {
		return subRequestContact;
	}
	
	/**
	 * Sets the sub request contact.
	 *
	 * @param subRequestContact the new sub request contact
	 */
	public void setSubRequestContact(String subRequestContact) {
		this.subRequestContact = subRequestContact;
	}
	
	/**
	 * Gets the sub request by.
	 *
	 * @return the sub request by
	 */
	public String getSubRequestBy() {
		return subRequestBy;
	}
	
	/**
	 * Sets the sub request by.
	 *
	 * @param subRequestBy the new sub request by
	 */
	public void setSubRequestBy(String subRequestBy) {
		this.subRequestBy = subRequestBy;
	}


	/**
	 * Sub Request Class to String method
	 */
	@Override
	public String toString() {
		return "SubRequest [subRequestNo=" + subRequestNo + ", subRequestDate="
				+ subRequestDate + ", subRequestType=" + subRequestType
				+ ", subRequestSubType=" + subRequestSubType
				+ ", subRequestAddress=" + subRequestAddress
				+ ", subRequestContact=" + subRequestContact
				+ ", subRequestBy=" + subRequestBy + "]";
	}
	
	
}
