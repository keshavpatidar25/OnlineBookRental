package dto;


/**
 * The Class SubscriptionPlan.
 */
public class SubscriptionPlan {
	
	/** The plan id. */
	private String planId;
	
	/** The no of books allowed to be issued at a time in plan, plan validity. */
	private int noOfBooksAllowed, planValidity;
	
	/** The plan price. */
	private float planPrice;
	
	/**
	 * Instantiates a new subscription plan.
	 */
	public SubscriptionPlan() {
	
	}
	
	/**
	 * Gets the plan id.
	 *
	 * @return the plan id
	 */
	public String getPlanId() {
		return planId;
	}
	
	/**
	 * Sets the plan id.
	 *
	 * @param planId the new plan id
	 */
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	
	/**
	 * Gets the no of books allowed.
	 *
	 * @return the no of books allowed
	 */
	public int getNoOfBooksAllowed() {
		return noOfBooksAllowed;
	}
	
	/**
	 * Sets the no of books allowed.
	 *
	 * @param noOfBooksAllowed the new no of books allowed
	 */
	public void setNoOfBooksAllowed(int noOfBooksAllowed) {
		this.noOfBooksAllowed = noOfBooksAllowed;
	}
	
	/**
	 * Gets the plan validity.
	 *
	 * @return the plan validity
	 */
	public int getPlanValidity() {
		return planValidity;
	}
	
	/**
	 * Sets the plan validity.
	 *
	 * @param planValidity the new plan validity
	 */
	public void setPlanValidity(int planValidity) {
		this.planValidity = planValidity;
	}
	
	/**
	 * Gets the plan price.
	 *
	 * @return the plan price
	 */
	public float getPlanPrice() {
		return planPrice;
	}
	
	/**
	 * Sets the plan price.
	 *
	 * @param planPrice the new plan price
	 */
	public void setPlanPrice(float planPrice) {
		this.planPrice = planPrice;
	}
	
	/**
	 * Subscription Plan Class to String method
	 */
	@Override
	public String toString() {
		return "SubscriptionPlan [planId=" + planId + ", noOfBooksAllowed="
				+ noOfBooksAllowed + ", planValidity=" + planValidity
				+ ", planPrice=" + planPrice + "]";
	}
	
}
