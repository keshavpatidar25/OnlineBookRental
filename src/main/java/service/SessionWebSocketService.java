package service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;


/**
 * Need tomcat-coyote.jar on class path, otherwise has compile error "the hierarchy of the type ... is inconsistent"
 *
 * Class Session Web Socket Service
 *
 */
public class SessionWebSocketService extends MessageInbound {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(SessionWebSocketService.class);

	/** The socket service out bound. */
	private WsOutbound socketServiceOutBound;
	
	/** The session. This will be used to store http session of user */
	private HttpSession session;
	
	/**
	 * Instantiates a new session web socket service.
	 * Sets the http session of a user into instance member of the session web socket service.
	 * @param httpServletRequest the http servlet request
	 */
	public SessionWebSocketService(HttpServletRequest httpServletRequest) {
		logger.info("Inside Session Web Socket Service Controller");
		session=httpServletRequest.getSession();
	}

	/**
	 * @see org.apache.catalina.websocket.StreamInbound#onOpen(org.apache.catalina.websocket.WsOutbound)
	 * On Open tells what is to be done when web socket connection is opened
	 */
	@Override
	public void onOpen(WsOutbound outbound) {
		logger.info("Opening Socket Connection");
		try {
			this.socketServiceOutBound = outbound;
		} catch (Exception e) {
			logger.error("Exception : "+e.getMessage(),e);
		}

	}

	/** 
	 * @see org.apache.catalina.websocket.StreamInbound#onClose(int)
	 * On Close tells what is to be done when web socket connection is closed
	 */
	@Override
	public void onClose(int status) {
		logger.info("Closing Socket Connection");
	}

	/**
	 * @see org.apache.catalina.websocket.MessageInbound#onBinaryMessage(java.nio.ByteBuffer)
	 * On Binary Message tells what is to be done when binary message is received from client
	 */
	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
		logger.info("Receiving Binary Message");
	}

	/**
	 * @see org.apache.catalina.websocket.MessageInbound#onTextMessage(java.nio.CharBuffer)
	 * On Text Message tells what is to be done when text message is received from client
	 */
	@Override
	protected void onTextMessage(CharBuffer inChar) throws IOException {
		logger.info("Receiving Text Message");
		try{
			CharBuffer userRole=null;
			if(session!=null){
				userRole = CharBuffer.wrap(SessionService.getSessionUserRole(session));
			} else{
				userRole = CharBuffer.wrap("");
			}
			this.socketServiceOutBound.writeTextMessage(userRole);
			this.socketServiceOutBound.flush();
			
		} catch(Exception e){
			logger.error("Exception : "+e.getMessage(),e);
		}
	}

}
