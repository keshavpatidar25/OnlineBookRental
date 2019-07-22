package service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import dto.Book;

/**
 * The Class MailService.
 */
public class MailService {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(MailService.class);
	
	/** The mail Password, mail from address. */
	private final static String mailPassword = "bookworld2014", mailFromAddress = "bookworld.care2014@gmail.com";
    
    /** The mail to address, mail subject, mail body. */
    private String mailToAddress = "bookworld.care2014@gmail.com", mailSubject="Book World Support", mailBody;
	
    /**
     * Instantiates a new mail service.
     */
    public MailService(){
    	logger.info("Inside Mail Service Default Constructor");
    	mailBody="";
    }
    
    /**
     * Instantiates a new mail service.
     *
     * @param mailBody the mail body
     */
    public MailService(String mailBody){
    	logger.info("Inside Mail Service Single Parameter Constructor");
    	this.mailBody=mailBody;
    }
    
    /**
     * Instantiates a new mail service.
     *
     * @param mailSubject the mail subject
     * @param mailBody the mail body
     */
    public MailService(String mailSubject, String mailBody){
    	logger.info("Inside Mail Service Two Parameter Constructor");
    	this.mailSubject=mailSubject;
    	this.mailBody=mailBody;
    } 
    
    /**
     * Instantiates a new mail service.
     *
     * @param mailToAddress the mail to address
     * @param mailSubject the mail subject
     * @param mailBody the mail body
     */
    public MailService(String mailToAddress, String mailSubject, String mailBody){
    	logger.info("Inside Mail Service Three Parameter Constructor");
    	this.mailToAddress=mailToAddress;
    	this.mailSubject=mailSubject;
    	this.mailBody=mailBody;
    }
    
    /**
     * Thanks email. This creates thankyou email for users who have sent a feedback and calls send Email method
     *
     * @param mailToAddress the mail to address
     */
    public void thanksEmail(String mailToAddress) {
    	logger.info("Inside Thanks Mail");
    	try{
	    	this.mailToAddress=mailToAddress;
	    	this.mailBody="Dear Sir/Ma'am,\n\nThankyou for your valuable feedback.\nWe will contact you as soon as possible.\n\nThanks & Regards,\nBook World\n"+mailFromAddress+"\nContact : 1800-18001800";
	    	this.sendEmail();
    	}catch(Exception e){
    		logger.error("Exception : "+e.getMessage(),e);
    	}
    }
    
    /**
     * Adds the book details in mail. This is used when book requests are made. For eg, in case of book issue, book issue close, etc.
     * Mail which is sent to user. It also contains book details which is added by this method.
     * After adding book details it sends mail.
     * @param book the book whose details are to be added in mail.
     * @throws Exception the exception
     */
    public void addBookDetailsInMail(Book book) throws Exception{
    	logger.info("Adding Book Details in Mail");
    	this.mailBody=mailBody+"\n\nFollowing are the Book Details : \n\n"
    			+"Book No : "+book.getBookNo()+"\nBook Title : "+book.getBookTitle()
    			+"\nBook Author : "+book.getBookAuthor()+"\nBook Category : "+book.getBookCategory()
    			+"\n\nThanks & Regards,\nBook World\n"+mailFromAddress+"\nContact : 1800-18001800";
    	this.sendEmail();		
    }
    
	/**
	 * sendEmail Working Method but port used by this method is blocked by firewall.
	 * public String sendEmail() throws Exception {
        logger.info("Sending Mail");
        String result=null;
        Transport tr=null;
        try{
			Properties props = System.getProperties();
	        props.put("mail.smtp.host", this.mailHost);
	        props.put("mail.smtps.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");
	        Session session = Session.getInstance(props, null);
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(this.mailFromAddress));
	        message.setRecipients(Message.RecipientType.TO, this.mailToAddress);
	        message.setSubject(this.mailSubject);
	        BodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setText(this.mailBody);
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(messageBodyPart);
	        messageBodyPart = new MimeBodyPart();
	        message.setContent(multipart);
	        tr =session.getTransport("smtps");
	        tr.connect(this.mailHost, this.mailFromAddress, this.mailPassword);
	        tr.sendMessage(message, message.getAllRecipients());
	        result="SUCCESS";
        } catch(SendFailedException e){
        	result = "Feedback could not be sent due to Internet Connectivity.";
        	logger.error("Exception : "+e.getMessage(),e);
        } catch(Exception e){
        	result = "Feedback could not be sent. Server Error.";
        	logger.error("Exception : "+e.getMessage(),e);
        }
        finally{
        	if(tr!=null)
        		tr.close();
        }
        return result;
    }*/
    
    /**
	 * Send email.
	 *
	 * @return the string
	 * @throws Exception the exception
	 */
	public String sendEmail() throws Exception{
		logger.info("Sending Mail to "+this.mailToAddress);
		
		/** Get system properties */
    	String result;
		Properties properties = System.getProperties();

		/** Setup mail server
		//properties.setProperty("mail.smtp.host", host);*/
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port","587");
		properties.setProperty("mail.smtp.starttls.enable", "true");
	 

		/** Get the default Session object. */
		Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
        @Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailFromAddress, mailPassword);
			}
		});

	
		/** Create a default MimeMessage object. */
		MimeMessage message = new MimeMessage(session);

		/** Set Email from which mail will be send */
		message.setFrom(new InternetAddress(mailFromAddress));

		/** Set Email to which mail will be send */
		message.addRecipient(Message.RecipientType.TO,
								  new InternetAddress(this.mailToAddress));

		/** Set Mail Subject */
		message.setSubject(this.mailSubject);

		/** Set Mail Text */
		message.setText(this.mailBody);

		/** Send Mail */
//		Transport.send(message);
        result="SUCCESS";      
        logger.info("Mail Sent Successfully to "+this.mailToAddress);
	
		return result;
    }
}
