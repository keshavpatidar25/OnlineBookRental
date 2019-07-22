package service;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import dao.BookDao;
import dao.UserDao;
import dto.Book;
import dto.User;
	 
/**
 * The Class SubscriptionMailSchedulerJob.
 * Tells the job which is to be performed by scheduler
 */
public class SchedulerJob implements Job{
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(SchedulerJob.class);
	
    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     * execute method is automatically called when scheduler starts.
     */
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
    	logger.info("Starting Scheduler Jobs");
    	this.addRecommendedBooks();
    	this.sendSubscriptionMails();
    }
    
    /**
     * Send subscription mails.
     * This method first finds all the users whose subscription plan will end in either 1 day, 7 days or 30 days. and send mail to them. 
     */
    public void sendSubscriptionMails(){
    	try {
    		logger.info("Subscription Mail Scheduler Started");
			List<Map<String,Object>> activeUsersList = new UserDao().getAllActiveUsers();
			Iterator<Map<String,Object>> activerUsersIterator = activeUsersList.iterator();
			User user;
			String mailToAddress, mailSubject, mailBody;
			MailService mail;
			long daysDifference;
			Map<String,Object> map = null;
			while(activerUsersIterator.hasNext()){
				map=activerUsersIterator.next();
				Date planEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(map.get("planEndDate").toString());
				Date today = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				daysDifference = (planEndDate.getTime()-today.getTime())/(24*60*60*1000);
				if(daysDifference==1||daysDifference==7||daysDifference==30){
					user = (User)map.get("user");
					mailToAddress = user.getUserEmail();
					mailSubject = "Book World Subscription Plan Alerts";
					mailBody = "Dear "+user.getUserName()+",\n\nYour "+map.get("planId").toString()+" Subscription Plan will expire in "+daysDifference+" days.\n\nThanks & Regards,\nBook World\nbookworld.care2014@gmail.com\nContact : 1800-18001800";
					mail = new MailService(mailToAddress, mailSubject, mailBody);
					mail.sendEmail();
				}
			}
		} catch (SQLException e) {
			logger.error("Exception : "+e.getMessage(),e);
		} catch (Exception e) {
			logger.error("Exception : "+e.getMessage(),e);
		}
    }
    
    /**
     * Adds the recommended books.
     * This method first gets all the users. Then one by one gets the recommended books for users. And adds them into database.
     */
    public void addRecommendedBooks(){
    	logger.info("Recommended Books Scheduler Started");
    	User user = null;
    	Book book = null;
    	List<Book> recommendedBooksList = null;
    	List<User> usersList = null;
    	Iterator<User> userIterator = null;
    	Iterator<Book> bookIterator = null;
    	String result = null;
    	try {
        	result = new BookDao().removeRecommendedBooks();
        	if(!result.equalsIgnoreCase("SUCCESS")){
        		System.out.println("Recommended Book Details couldnot be deleted. "+result);
        		return;
        	}
			usersList = new UserDao().getAllUsers();
			userIterator = usersList.iterator();
			while(userIterator.hasNext()){
				user = userIterator.next();
				recommendedBooksList = new BookDao().getRecommendedBooksList(user);
				bookIterator = recommendedBooksList.iterator();
				while(bookIterator.hasNext()){
					book = bookIterator.next();
					result = new BookDao().addRecommendedBook(user, book);
					if(!result.equalsIgnoreCase("SUCCESS")){
		        		System.out.println("Recommended Book Details for book no "+book.getBookNo()+" and for user email "+user.getUserEmail()+" couldnot be added. "+result);
		        	}
				}
			}
		} catch (SQLException e) {
			logger.error("Exception : "+e.getMessage(),e);
		} catch (Exception e) {
			logger.error("Exception : "+e.getMessage(),e);
		}
    }
}