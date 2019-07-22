package dao;

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
import dto.User;


/**
 * The Class UserDao.
 */
public class UserDao {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(UserDao.class);
	
	/** Database connection singleton class object. */
	private MyConnection conn = new MyConnection();

	/**
	 * Login.
	 *
	 * @param user the user whose login credentials is to be found
	 * @return the status of the login operation
	 * @throws SQLException the SQL exception
	 
	public String login(User user)throws SQLException{
		logger.info("Inside User Login Method.");
		Connection con = null;
		String result;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con
					.prepareStatement("Select * from User where user_email=? and user_pass =?");
			ps.setString(1, user.getUserEmail());
			ps.setString(2, user.getUserPass());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				this.getUserDetails(user);
				result = "SUCCESS";
			} else {
				user=null;
				result = "Invalid Email Or Password";
			}
		} catch(SQLException e){
			result = "ERROR : Database Error";
		}
		catch(Exception e){
			result = "ERROR : Server Error";
		}
		finally {
			if (con != null)
				con.close();
		}
		return result;
	}
	*/
	
	/**
	 * Gets the user details.
	 *
	 * @param user the user whose details are to be found
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public void getUserDetails(User user)throws SQLException, Exception{
		logger.info("Getting User Details");
		Connection con = null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con
					.prepareStatement("Select * from User where user_email=?");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user.setUserPass(rs.getString("user_pass"));
				user.setUserName(rs.getString("user_name"));
				user.setUserAddress(rs.getString("user_address"));
				user.setUserContact(rs.getString("user_contact"));
				user.setUserRole(rs.getString("user_role"));
			} else {
				user.setUserEmail(null);
			}
		} 
		finally {
			if (con != null)
				con.close();
		}
	}
	
	/**
	 * Register.
	 *
	 * @param user the user who is to be registered
	 * @return the status of the register operation whether success or failure
	 * @throws SQLException the SQL exception
	 */
	public String register(User user) throws SQLException {
		logger.info("Registering "+user.getUserRole());
		Connection con = null;
		String result;
		try {
			con= conn.getConnection();
			PreparedStatement ps = con
					.prepareStatement("insert into user values(?,?,?,?,?,?)");
			ps.setString(1, user.getUserEmail());
			ps.setString(2, user.getUserPass());
			ps.setString(3, user.getUserName());
			ps.setString(4, user.getUserAddress());
			ps.setString(5, user.getUserContact());
			ps.setString(6, user.getUserRole());
			ps.executeUpdate();
			result = "SUCCESS";
			logger.info("Registration Successful");
		} catch(SQLException e){
			result="Database Error";
			logger.error("Database Error while registering. Error : "+e.getMessage(),e);
		} catch(Exception e){
			result="Server Error";
			logger.error("Server Error while registering. Error : "+e.getMessage(),e);
		}
		finally {
			if (con != null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Update user details.
	 *
	 * @param newUser the new user details
	 * @param oldUser the old user details
	 * @return the status of the operation whether success or failure
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String updateUserDetails(User newUser, User oldUser) throws SQLException,Exception{
		logger.info("Updating User Details");
		Connection con = null;
		String result=null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE user SET user_name=?,user_email=?,user_address=?,user_contact=? where user_email=?");
			ps.setString(1, newUser.getUserName());
			ps.setString(2, newUser.getUserEmail());
			ps.setString(3, newUser.getUserAddress());
			ps.setString(4, newUser.getUserContact());
			ps.setString(5, oldUser.getUserEmail());
			ps.executeUpdate();
			result="SUCCESS";
		}finally {
			if (con != null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Check user exists.
	 *
	 * @param user the user whose existence is to be checked
	 * @return the status of the operation
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String checkUserExists(User user) throws SQLException, Exception{
		logger.info("Checking whether user exists or not");
		String result;
		Connection con=null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("Select user_email from User where user_email=?");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				result = "User Already Exists";
			} else{
				result = "SUCCESS";
			}
		}finally{
			if(con!=null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Update user password.
	 *
	 * @param user the user whose password is to be updated. Also contains new password for that user.
	 * @return the status of change password operations.
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String updateUserPassword(User user) throws SQLException,Exception{
		logger.info("Updating User Password");
		Connection con = null;
		String result=null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE user SET user_pass=? where user_email=?");
			ps.setString(1, user.getUserPass());
			ps.setString(2, user.getUserEmail());
			ps.executeUpdate();
			result="SUCCESS";
		}finally {
			if (con != null)
				con.close();
		}
		return result;
	}
	
	/**
	 * Gets the all active users.
	 *
	 * @return the all active users
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getAllActiveUsers() throws SQLException,Exception{
		logger.info("Getting All Active Users");
		List<Map<String,Object>> activeUsersList = null;
		Map<String,Object> map=null;
		Connection con = null;
		User user = null;
		try {
			con = conn.getConnection();
			activeUsersList = new ArrayList<Map<String,Object>>();
			PreparedStatement ps = con.prepareStatement("SELECT user_name,u.user_email,user_contact, sp.plan_id, plan_start_date, DATE_ADD(plan_start_date,INTERVAL plan_validity MONTH) AS plan_end_date FROM user u INNER JOIN user_subscription us ON u.user_email=us.user_email INNER JOIN subscription_plan sp ON us.plan_id=sp.plan_id WHERE DATE_ADD(plan_start_date,INTERVAL plan_validity MONTH)>=CAST(NOW() AS DATE) ORDER BY user_name");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map=new HashMap<String, Object>();
				user=new User();
				user.setUserName(rs.getString("user_name"));
				user.setUserEmail(rs.getString("user_email"));
				user.setUserContact(rs.getString("user_contact"));
				map.put("user", user);
				map.put("planId", rs.getString("plan_id"));
				map.put("planStartDate", rs.getDate("plan_start_date"));
				map.put("planEndDate", rs.getDate("plan_end_date"));
				activeUsersList.add(map);
			}
		}finally {
			if (con != null)
				con.close();
		}
		return activeUsersList;
	}
	
	/**
	 * Gets the all users.
	 *
	 * @return the list of all users email
	 * @throws SQLException the SQL exception
	 */
	public List<User> getAllUsers() throws SQLException{
		logger.info("Getting All Users");
		List<User> usersList = null;
		Connection con = null;
		User user = null;
		try {
			con = conn.getConnection();
			usersList = new ArrayList<User>();
			PreparedStatement ps = con.prepareStatement("SELECT user_email FROM user");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				user=new User();
				user.setUserEmail(rs.getString("user_email"));
				usersList.add(user);
			}
		}finally {
			if (con != null)
				con.close();
		}
		return usersList;
	}
	
	/**
	 * Gets the user password.
	 *
	 * @param user the user whose password is to be fetched
	 * @return the user password
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String getUserPassword(User user) throws SQLException, Exception{
		logger.info("Getting User Password");
		String userPass;
		Connection con=null;
		try {
			con = conn.getConnection();
			PreparedStatement ps = con.prepareStatement("Select user_pass from User where user_email=?");
			ps.setString(1, user.getUserEmail());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				logger.info("Valid User while getting password");
				userPass=rs.getString("user_pass");
			} else{
				logger.info("Invalid User while getting password");
				userPass="";
			}
		}finally{
			if(con!=null)
				con.close();
		}
		return userPass;
	}
}
