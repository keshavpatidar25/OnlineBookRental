package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import conn.MyConnection;
import dto.Request;
import dto.SubRequest;


/**
 * The Class SubRequestDao.
 */
public class SubRequestDao {
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(SubRequestDao.class);
	
	/** Database connection singleton class object. */
	private MyConnection conn = new MyConnection();
	
	/**
	 * Gets the sub requests list.
	 *
	 * @param request the request whose sub requests are to be fetched
	 * @return the sub requests list
	 * @throws SQLException the SQL exception
	 */
	public List<SubRequest> getSubRequestsList(Request request) throws SQLException{
		logger.info("Getting Sub Requests List");
		Connection con= null;
		List<SubRequest> subRequestsList=null;
		SubRequest subRequest=null;
		try{
			con = conn.getConnection();
			subRequestsList = new ArrayList<SubRequest>();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM book_sub_request WHERE request_no = ?");
			ps.setInt(1, request.getRequestNo());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				subRequest = new SubRequest();
				subRequest.setSubRequestNo(rs.getInt("sub_request_no"));
				subRequest.setSubRequestType(rs.getString("request_type"));
				subRequest.setSubRequestSubType(rs.getString("request_sub_type"));
				subRequest.setSubRequestDate(rs.getTimestamp("request_date"));
				subRequest.setSubRequestAddress(rs.getString("address"));
				subRequest.setSubRequestContact(rs.getString("contact"));
				subRequest.setSubRequestBy(rs.getString("request_by"));
				subRequestsList.add(subRequest);
			}
		} finally{
			if(con!=null)
				con.close();
		}
		return subRequestsList;
	}
}
