package service;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import dto.User;

/**
 * The Class SessionService.
 */
public class SessionService {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(SessionService.class);
	
	/**
	 * First checks login credentials of the user. If login is successful. Then sets the session 
	 *
	 * @param user the user whose session is to be set
	 * @param session the Http session which is to be set
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public static void setSession(User user, HttpSession session) throws SQLException,Exception{
		logger.info("Setting Http Session");
		session.setAttribute("user", user);
	}
	
	/**
	 * Gets the session user role.
	 *
	 * @param session the Http Session from which user role is to be fetched
	 * @return the session user role
	 * @throws Exception the exception
	 */
	public static String getSessionUserRole(HttpSession session) throws Exception{
		logger.info("Getting User Role");
		String userRole=null;
		try{
			if(session!=null && session.getAttribute("user")!=null){		
				User user = (User)session.getAttribute("user");
				userRole= user.getUserRole();
			} else{
				userRole= "";
			}
		} catch(IllegalStateException e){
			userRole= "";
			logger.error("Exception : "+e.getMessage(),e);
		}
		return userRole;
	}
	
	/**
	 * Clear session.
	 *
	 * @param session the Http Session which is to be cleared
	 * @throws Exception the exception
	 */
	public static void clearSession(HttpSession session) throws Exception{
		logger.info("Invalidating Session");
		if(session!=null && session.getAttribute("user")!=null){
			session.removeAttribute("user");
			session.invalidate();
		}
	}
}
