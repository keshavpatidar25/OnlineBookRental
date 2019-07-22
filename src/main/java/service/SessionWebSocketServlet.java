package service;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.log4j.Logger;


/**
 * WebSocketServlet is contained in catalina.jar. It also needs servlet-api.jar
 * In case of apache tomcat web server and maven project,
 * set scope provided /scope in apache catalina maven dependency. 
 *
 * Annotation WebServlet provides path to access web socket from client
 */
@WebServlet("/sessionWebSocket")
public class SessionWebSocketServlet extends WebSocketServlet {

	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(SessionWebSocketServlet.class);
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** for new clients, <sessionId, streamInBound>
	* The clients details are stored in this. 
	*/
	private static ConcurrentHashMap<String, StreamInbound> clients = new ConcurrentHashMap<String, StreamInbound>();

	/**
	 * Creates the web socket inbound. This method is called when 
	 *
	 * @param protocol the protocol used for web socket communication
	 * @param httpServletRequest the http servlet request
	 * @return the stream inbound
	 */
	protected StreamInbound createWebSocketInbound(String protocol,	HttpServletRequest httpServletRequest) {
		logger.info("Creating Web Socket Inbound");
		
		/** Gets the Http Session of the client*/
		HttpSession session = httpServletRequest.getSession();

		/** find client on basis of http session. if not found, add client to clients list */
		StreamInbound client = clients.get(session.getId());
		if (client != null) {
			return client;

		} else {
			client = new SessionWebSocketService(httpServletRequest);
			clients.put(session.getId(), client);
		}

		return client;
	}

	/**
	 * Gets the client.
	 *
	 * @param sessionId the http session id of the client 
	 * @return the client
	 */
	public StreamInbound getClient(String sessionId) {
		logger.info("Getting Session Id");
		return clients.get(sessionId);
	}

	/**
	 * Adds the client.
	 *
	 * @param sessionId the http session id of the client
	 * @param streamInBound the stream in bound
	 */
	public void addClient(String sessionId, StreamInbound streamInBound) {
		logger.info("Adding Client");
		clients.put(sessionId, streamInBound);
	}

	/**
	 * @see org.apache.catalina.websocket.WebSocketServlet#createWebSocketInbound(java.lang.String)
	 * Web Socket Servlet method to be overriden
	 */
	@Override
	protected StreamInbound createWebSocketInbound(String arg0) {
		return null;
	}
}