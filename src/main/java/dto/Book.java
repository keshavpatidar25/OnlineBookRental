package dto;


import java.sql.Date;
import java.util.Arrays;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class Book.
 */
@XmlRootElement
public class Book {
	
	/** book No, book Quantity, book available count. */
	private int bookNo, bookQty, bookAvailable;
	
	/** book Title, book Author, book Category, book Publisher, book description. */
	private String bookTitle, bookAuthor, bookCategory, bookPublisher, bookDescription;
	
	/** The book cover. */
	private byte[] bookCover;
	
	/** The book arrival date. */
	private Date bookArrival;
	
	/** The book price. */
	private float bookPrice;
	
	/**
	 * Instantiates a new book.
	 */
	public Book() {
		
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
	 * Gets the book quantity.
	 *
	 * @return the book quantity
	 */
	public int getBookQty() {
		return bookQty;
	}

	/**
	 * Sets the book quantity.
	 *
	 * @param bookQty the new book quantity
	 */
	public void setBookQty(int bookQty) {
		this.bookQty = bookQty;
	}

	/**
	 * Gets the book title.
	 *
	 * @return the book title
	 */
	public String getBookTitle() {
		return bookTitle;
	}

	/**
	 * Sets the book title.
	 *
	 * @param bookTitle the new book title
	 */
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	/**
	 * Gets the book author.
	 *
	 * @return the book author
	 */
	public String getBookAuthor() {
		return bookAuthor;
	}

	/**
	 * Sets the book author.
	 *
	 * @param bookAuthor the new book author
	 */
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	/**
	 * Gets the book category.
	 *
	 * @return the book category
	 */
	public String getBookCategory() {
		return bookCategory;
	}

	/**
	 * Sets the book category.
	 *
	 * @param bookCategory the new book category
	 */
	public void setBookCategory(String bookCategory) {
		this.bookCategory = bookCategory;
	}

	/**
	 * Gets the book publisher.
	 *
	 * @return the book publisher
	 */
	public String getBookPublisher() {
		return bookPublisher;
	}

	/**
	 * Sets the book publisher.
	 *
	 * @param bookPublisher the new book publisher
	 */
	public void setBookPublisher(String bookPublisher) {
		this.bookPublisher = bookPublisher;
	}

	/**
	 * Gets the book description.
	 *
	 * @return the book description
	 */
	public String getBookDescription() {
		return bookDescription;
	}

	/**
	 * Sets the book description.
	 *
	 * @param bookDescription the new book description
	 */
	public void setBookDescription(String bookDescription) {
		this.bookDescription = bookDescription;
	}

	/**
	 * Gets the book cover.
	 *
	 * @return the book cover
	 */
	public byte[] getBookCover() {
		return bookCover;
	}

	/**
	 * Sets the book cover.
	 *
	 * @param bookCover the new book cover
	 */
	public void setBookCover(byte[] bookCover) {
		this.bookCover = bookCover;
	}

	/**
	 * Gets the book arrival date.
	 *
	 * @return the book arrival date
	 */
	public Date getBookArrival() {
		return bookArrival;
	}

	/**
	 * Sets the book arrival date.
	 *
	 * @param bookArrival the new book arrival date
	 */
	public void setBookArrival(Date bookArrival) {
		this.bookArrival = bookArrival;
	}
	
	/**
	 * Gets the book price.
	 *
	 * @return the book price
	 */
	public float getBookPrice() {
		return bookPrice;
	}

	/**
	 * Sets the book price.
	 *
	 * @param bookPrice the new book price
	 */
	public void setBookPrice(float bookPrice) {
		this.bookPrice = bookPrice;
	}

	/**
	 * Gets the book available count.
	 *
	 * @return the book available count
	 */
	public int getBookAvailable() {
		return bookAvailable;
	}

	/**
	 * Sets the book available count.
	 *
	 * @param bookAvailable the new book available
	 */
	public void setBookAvailable(int bookAvailable) {
		this.bookAvailable = bookAvailable;
	}
	
	/** 
	 * Book Class toString method
	 */
	@Override
	public String toString() {
		return "Book [bookNo=" + bookNo + ", bookQty=" + bookQty
				+ ", bookAvailable=" + bookAvailable + ", bookTitle="
				+ bookTitle + ", bookAuthor=" + bookAuthor + ", bookCategory="
				+ bookCategory + ", bookPublisher=" + bookPublisher
				+ ", bookDescription=" + bookDescription + ", bookCover="
				+ Arrays.toString(bookCover) + ", bookArrival=" + bookArrival
				+ ", bookPrice=" + bookPrice + "]";
	}	
	
}
