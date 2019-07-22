package service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.BookDao;


/**
 * The Class BooksRestService.
 * Annotation Path specifies the path which is to be used to access web service.
 */
@Path("/books")
public class BooksRestService {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(BooksRestService.class);
			
	/**
	 * Gets the books list.
	 * Annotation GET specifies parameters are coming by GET method.
	 * Annotation Path specifies the additional path to be added to web service path to access this method. {} implies path parameter
	 * Annotation Produces specifies what type to data is returned by this method. MediaType.APPLICATION_JSON implies JSON data 
	 * @param bookSearchString the book search string
	 * @param jsonCallBack the JsonP Callback function for CORS(Cross Origin Resource Sharing)
	 * @return the books list
	 */
	@GET
	@Path("{bookSearchString}")
	@Produces("application/javascript")
	public Response getBooksList(@PathParam("bookSearchString") String bookSearchString,@QueryParam("callback") String jsonCallBack){
		logger.info("Getting Book List using Book Rest Service");
		ObjectMapper mapper = null;
		Map<String,Object> map  = null;
		String jsonBooksList=null;
		try{
			map = new HashMap<String, Object>();
			mapper = new ObjectMapper();
			map.put("booksList",new BookDao().getBooksListByAll(bookSearchString));
			map.put("result", "SUCCESS");
			jsonBooksList = mapper.writeValueAsString(map);
		} catch(SQLException e){
			map.put("result", "ERROR : Database Error.");
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			map.put("result", "ERROR : Server Error.");
			logger.error("Exception : "+e.getMessage(),e);
		}
		return Response.status(200).entity(jsonCallBack+"("+jsonBooksList+");").build();
	}
	
	/**
	 * Gets the books list on basis of title.
	 * Annotation GET specifies parameters are coming by GET method.
	 * Annotation Path specifies the additional path to be added to web service path to access this method. {} implies path parameter
	 * Annotation Produces specifies what type to data is returned by this method. MediaType.APPLICATION_JSON implies JSON data
	 * @param bookSearchString the book search string
	 * @param jsonCallBack the JsonP Callback function for CORS(Cross Origin Resource Sharing)
	 * @return the books list by title
	 */
	@GET
	@Path("title/{bookSearchString}")
	@Produces("application/javascript")
	public Response getBooksListByTitle(@PathParam("bookSearchString") String bookSearchString,@QueryParam("callback") String jsonCallBack){
		logger.info("Getting Book List by title using Book Rest Service");
		ObjectMapper mapper = null;
		Map<String,Object> map  = null;
		String jsonBooksList=null;
		try{
			map = new HashMap<String, Object>();
			mapper = new ObjectMapper();
			map.put("booksList",new BookDao().getBooksListByTitle(bookSearchString));
			map.put("result", "SUCCESS");
			jsonBooksList = mapper.writeValueAsString(map);
		} catch(SQLException e){
			map.put("result", "ERROR : Database Error.");
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			map.put("result", "ERROR : Server Error.");
			logger.error("Exception : "+e.getMessage(),e);
		}
		return Response.status(200).entity(jsonCallBack+"("+jsonBooksList+");").build();
	}
	
	/**
	 * Gets the books list on basis of author.
	 * Annotation GET specifies parameters are coming by GET method.
	 * Annotation Path specifies the additional path to be added to web service path to access this method. {} implies path parameter
	 * Annotation Produces specifies what type to data is returned by this method. MediaType.APPLICATION_JSON implies JSON data
	 * @param bookSearchString the book search string
	 * @param jsonCallBack the JsonP Callback function for CORS(Cross Origin Resource Sharing)
	 * @return the books list by author
	 */
	@GET
	@Path("author/{bookSearchString}")
	@Produces("application/javascript")
	public Response getBooksListByAuthor(@PathParam("bookSearchString") String bookSearchString,@QueryParam("callback") String jsonCallBack){
		logger.info("Getting Book List by author using Book Rest Service");
		ObjectMapper mapper = null;
		Map<String,Object> map  = null;
		String jsonBooksList=null;
		try{
			map = new HashMap<String, Object>();
			mapper = new ObjectMapper();
			map.put("booksList",new BookDao().getBooksListByAuthor(bookSearchString));
			map.put("result", "SUCCESS");
			jsonBooksList = mapper.writeValueAsString(map);
		} catch(SQLException e){
			map.put("result", "ERROR : Database Error.");
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			map.put("result", "ERROR : Server Error.");
			logger.error("Exception : "+e.getMessage(),e);
		}
		return Response.status(200).entity(jsonCallBack+"("+jsonBooksList+");").build();
	}
	
	/**
	 * Gets the books list on basis of category.
	 * Annotation GET specifies parameters are coming by GET method.
	 * Annotation Path specifies the additional path to be added to web service path to access this method. {} implies path parameter
	 * Annotation Produces specifies what type to data is returned by this method. MediaType.APPLICATION_JSON implies JSON data
	 * @param bookSearchString the book search string
	 * @param jsonCallBack the JsonP Callback function for CORS(Cross Origin Resource Sharing)
	 * @return the books list by category
	 */
	@GET
	@Path("category/{bookSearchString}")
	@Produces("application/javascript")
	public Response getBooksListByCategory(@PathParam("bookSearchString") String bookSearchString,@QueryParam("callback") String jsonCallBack){
		logger.info("Getting Book List by category using Book Rest Service");
		ObjectMapper mapper = null;
		Map<String,Object> map  = null;
		String jsonBooksList=null;
		try{
			map = new HashMap<String, Object>();
			mapper = new ObjectMapper();
			map.put("booksList",new BookDao().getBooksListByCategory(bookSearchString));
			map.put("result", "SUCCESS");
			jsonBooksList = mapper.writeValueAsString(map);
		} catch(SQLException e){
			map.put("result", "ERROR : Database Error.");
			logger.error("Exception : "+e.getMessage(),e);
		} catch(Exception e){
			map.put("result", "ERROR : Server Error.");
			logger.error("Exception : "+e.getMessage(),e);
		}
		return Response.status(200).entity(jsonCallBack+"("+jsonBooksList+");").build();
	}
}
