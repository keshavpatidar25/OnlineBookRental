package service;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import dao.BookDao;
import dao.RequestDao;
import dto.Book;
import dto.User;


/**
 * The Class BookService.
 */
public class BookService {
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(BookService.class);
	
	/**
	 * Check book issue state(whether book is already issued by user or not AND whether book is available for issuing or not) before issuing book. 
	 * If book is not already issued by user and book is available, then issue book else don't issue book
	 * @param user the user who wants to issue book
	 * @param book the book whose book issue state is to be found
	 * @return the status of request
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String checkBookStatusBeforeIssue(User user, Book book) throws SQLException, Exception{
		logger.info("Checking Status of Book Before Issuing");
		String result=null;
		int bookIssuedOrNot = new RequestDao().getBookIssueStatus(user, book);
		if(bookIssuedOrNot==0){
			new BookDao().getBookAvailablity(book);
			if(book.getBookAvailable()>0){
				result = new RequestDao().issueBook(user, book);
			} else{
				result="Book is not available. Kindly refresh the page to verify.";
			}
		} else {
			result="Book is already issued. Kindly refresh the page to verify.";
		}
		return result;
	}
	
	/**
	 * Check availability before update. If issue book count is greater than new quantity of the book then stop update operation else update book
	 *
	 * @param book the book which is to be updated
	 * @return the status of the operation
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String checkAvailabilityBeforeUpdate(Book book) throws SQLException, Exception{
		logger.info("Checking Availability of Book Before Updating");
		String result=null;
		Book book1 = new Book();
		book1.setBookNo(book.getBookNo());
		new BookDao().getBookDetails(book1);
		new BookDao().getBookAvailablity(book);
		int bookIssueCount = book1.getBookQty()-book.getBookAvailable();
		if(bookIssueCount>book.getBookQty()){
			result="Book cannot be updated as book quantity is less than issue count ("+bookIssueCount+") of book.";
		} else{
			result=new BookDao().updateBook(book);
		}
		return result;
	}
	
	/**
	 * Check availability before delete. If book available count is equal to old book quantity (i.e. if book is not issued) then delete else stop delete operation 
	 *
	 * @param book the book
	 * @return the status of the operation
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public String checkAvailabilityBeforeDelete(Book book) throws SQLException, Exception{
		logger.info("Checking Availability of Book Before Deleting");
		String result=null;
		new BookDao().getBookAvailablity(book);
		new BookDao().getBookDetails(book);
		if(book.getBookAvailable()!=book.getBookQty()){
			result="Book cannot be deleted as book is issued";
		} else{
			result=new BookDao().deleteBook(book);
		}
		return result;
	}

}
