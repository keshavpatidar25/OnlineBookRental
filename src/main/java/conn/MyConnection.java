package conn;

import java.sql.*;

import org.apache.log4j.Logger;

/**
 * A singleton database access class.
 */
public final class MyConnection {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(MyConnection.class);
	
	/** Connection Object. */
	private Connection conn;
	
	/** URL of Connection */
	private final static String url = "jdbc:mysql://localhost:3306/";
	
	/** Database name. */
	private final static String dbName = "onlinebookrental";
	
	/** JDBC Driver. */
	private final static String driver = "com.mysql.jdbc.Driver";
	
	/** User name for database connectivity. */
	private final static String userName = "root";
	
	/** The password for database connectivity. */
	private final static String password = "yahoo";

	/**
	 * Instantiates a new my connection.
	 */
	public MyConnection() {
		logger.info("Creating New Database Connection");
		try {
			Class.forName(driver).newInstance();
			this.conn = (Connection) DriverManager.getConnection(url + dbName,
					userName, password);
		} catch (Exception e) {
			logger.error("Exception : "+e.getMessage(),e);
		}
	}

	/**
	 * Gets the connection.
	 *
	 * @return Database connection object
	 * @throws SQLException the SQL exception
	 */
	public synchronized Connection getConnection() throws SQLException{
		logger.info("Getting Database Connection");
		if (conn == null || conn.isClosed())
			return new MyConnection().conn;
		else
			return conn;
	}


}
