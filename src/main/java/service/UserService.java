package service;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import dao.UserDao;
import dto.User;

/**
 * The Class UserService.
 */
public class UserService {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(UserService.class);
	
	/**
	 * Login.
	 *
	 * @param user the user who wants to log in
	 * @param session the Http session which is to be set in case of successful login
	 * @return the status of the operation
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String login(User user, HttpSession session) throws SQLException, Exception{
		logger.info("Inside Login Service");
		UserDao ud = new UserDao();
		String result=null;
		if(user.getUserPass().equals(ud.getUserPassword(user))){
			ud.getUserDetails(user);
			user.setUserPass("");
			SessionService.setSession(user, session);
			result="SUCCESS";
		} else{
			SessionService.clearSession(session);
			result="Invalid User Email or Password";
		}
		return result;
	}
}
