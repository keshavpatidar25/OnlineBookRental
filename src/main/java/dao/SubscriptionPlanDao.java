package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import conn.MyConnection;
import dto.SubscriptionPlan;
import dto.User;


/**
 * The Class SubscriptionPlanDao.
 */
public class SubscriptionPlanDao {

	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(SubscriptionPlanDao.class);
			
	/** Database connection singleton class object. */
	private MyConnection conn = new MyConnection();
	
	/**
	 * Gets the all subscription plans.
	 *
	 * @return the all subscription plans
	 * @throws SQLException the SQL exception
	 */
	public List<SubscriptionPlan> getAllPlans() throws SQLException{
		logger.info("Getting All Subscription Plans List");
		Connection con = null;
		List<SubscriptionPlan> planList = new ArrayList<SubscriptionPlan>();
		SubscriptionPlan sp=null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con
					.prepareStatement("Select * from subscription_plan");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				sp = new SubscriptionPlan();
				sp.setPlanId(rs.getString("plan_id"));
				sp.setNoOfBooksAllowed(rs.getInt("plan_books_allowed"));
				sp.setPlanValidity(rs.getInt("plan_validity"));
				sp.setPlanPrice(rs.getFloat("plan_price"));
				planList.add(sp);
			}
		} 
		finally {
			if (con != null)
				con.close();
		}
		return planList;
	}
	
	/**
	 * Links a plan to a user
	 *
	 * @param plan the plan that user wants to subscribe to.
	 * @param user the user who wants to subscribe to a plan.
	 * @return the status of operation whether success or failure
	 * @throws SQLException the SQL exception
	 */
	public String addPlan(SubscriptionPlan plan, User user) throws SQLException{
		logger.info("Adding (Linking) Subscription Plan to User");
		Connection con = null;
		String result;
		try {
			Date date=new Date(new java.util.Date().getTime());
			con = conn.getConnection();
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO USER_SUBSCRIPTION VALUES(?,?,?)");
			ps.setString(1, user.getUserEmail());
			ps.setString(2, plan.getPlanId());
			ps.setDate(3, date);
			ps.executeUpdate();
			result="SUCCESS";
		} catch(SQLException e){
			result="ERROR : Database Error.";
		} catch(Exception e){
			result="ERROR : Server Error.";
		}
		finally {
			if (con != null)
				con.close();
		}
		
		return result;
	}
	
	/**
	 * Gets the active plan.
	 *
	 * @param user the user whose active plan is to be found
	 * @return the active plan of the user
	 * @throws SQLException the SQL exception
	 */
	public Map<String, Object> getActivePlan(User user) throws SQLException{
		logger.info("Getting Active Subscription Plan");
		Connection con = null;
		SubscriptionPlan plan = new SubscriptionPlan();
		Map<String, Object> map = new HashMap<String, Object>();
		String result=null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con
					.prepareStatement("SELECT *, DATE_ADD(plan_start_date,INTERVAL plan_validity MONTH) AS plan_end_date FROM USER_SUBSCRIPTION US, SUBSCRIPTION_PLAN SP where US.plan_id = SP.plan_id AND US.user_email=? AND DATE_ADD(plan_start_date,INTERVAL plan_validity MONTH)>=CAST(NOW() AS DATE) ORDER BY plan_start_date DESC LIMIT 1");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				plan.setNoOfBooksAllowed(rs.getInt("plan_books_allowed"));
				plan.setPlanPrice(rs.getFloat("plan_price"));
				plan.setPlanId(rs.getString("US.plan_id"));
				plan.setPlanValidity(rs.getInt("plan_validity"));
				map.put("user", user);
				map.put("plan", plan);
				map.put("planStartDate", rs.getString("plan_start_date"));
				map.put("planEndDate", rs.getString("plan_end_date"));
				result="SUCCESS";
			} else{
				result="NOT SUBSCRIBED";
			}
		} catch(SQLException e){
			result="ERROR : Database Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result="ERROR : Server Error.";
			logger.error("Exception : "+e.getMessage(),e);
		}
		finally {
			if (con != null)
				con.close();
		}
		map.put("result", result);
		return map;
	}
	
	/**
	 * Gets the plan history.
	 *
	 * @param user the user whose subscription plan history is to be found
	 * @return the plan history of user
	 * @throws SQLException the SQL exception
	 */
	public Map<String, Object> getPlanHistory(User user) throws SQLException{
		logger.info("Getting Subscription Plan History");
		Connection con = null;
		SubscriptionPlan plan = null;
		List<Map<String, Object>> planList=null;
		Map<String, Object> returnList=null;
		Map<String, Object> map=null;
		String result=null;
		try {
			con = conn.getConnection();
			planList = new ArrayList<Map<String, Object>>();
			PreparedStatement ps = con
					.prepareStatement("SELECT *, DATE_ADD(plan_start_date,INTERVAL plan_validity MONTH) AS plan_end_date FROM USER_SUBSCRIPTION US, SUBSCRIPTION_PLAN SP where US.plan_id = SP.plan_id AND US.user_email=? AND DATE_ADD(plan_start_date,INTERVAL plan_validity MONTH)<CAST(NOW() AS DATE)");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap<String, Object>();
				plan = new SubscriptionPlan();
				plan.setNoOfBooksAllowed(rs.getInt("plan_books_allowed"));
				plan.setPlanPrice(rs.getFloat("plan_price"));
				plan.setPlanId(rs.getString("US.plan_id"));
				plan.setPlanValidity(rs.getInt("plan_validity"));
				map.put("user", user);
				map.put("plan", plan);
				map.put("planStartDate", rs.getString("plan_start_date"));
				map.put("planEndDate", rs.getString("plan_end_date"));
				planList.add(map);
			}
			result="SUCCESS";
		} catch(SQLException e){
			result="ERROR : Database Error.";
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			result="ERROR : Server Error.";
			logger.error("Exception : "+e.getMessage(),e);
		}
		finally {
			if (con != null)
				con.close();
		}
		
		returnList= new HashMap<String, Object>();
		returnList.put("planList",planList);
		returnList.put("result",result);
		return returnList;
	}
	
}
