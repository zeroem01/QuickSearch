package dummyapp;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.cit.idm.util.NonceManager;
import edu.cornell.cit.idm.util.UserFeedbackMessages;

// this is an object that will go into the HttpSession, and retain user-specific state for 
// this user. In particular, it's used to preserve error message across pages/requests (e.g.
// during redirects), and to support the anti-csrf nonce.

public class UserSession {
	private NonceManager nonceManager;
	private UserFeedbackMessages feedback;
	private Date started;
	private String username = "";
	private String remoteAddr = "";
		
	// STATIC - to support using this as a sort of "session-based singleton".
	// if the object already exists in the session, it'll return a reference 
	// to it.
	// otherwise, if it doesn't already exist there, it'll create it there
	// and then return a reference to it.
	static UserSession getInstance( HttpServletRequest req ) throws ServletException {
	
		final Log l = LogFactory.getLog( dummyapp.UserSession.class );
		HttpSession hs = req.getSession();
	
		String remoteUser = req.getRemoteUser();
		// Just for now :)
		remoteUser = "se10";

		if( remoteUser == null ) {
			String msg = "remoteUser is null -- is authn configured?";
			l.error( msg );
			throw new ServletException(msg);
		}
		
	    UserSession us = (UserSession) hs.getAttribute("userSession");
	    if ( us != null ) {
	    	if ( !remoteUser.equals( us.getUsername() ) || !req.getRemoteAddr().equals( us.getRemoteAddr() ) )  {
	    		l.warn( us.getLogString() + "Sanity check failed - mismatch between session username and request remoteUser ["+ remoteUser 
	    			+"] or session remoteAddr and request remoteAddr ["+ req.getRemoteAddr() +"]. Session hijack? Restarting session...");
	    		us = null;
	    	}
	    }
	    
	    if ( us == null ) {
	    	us = new UserSession();
	        us.setUsername( remoteUser );
	        us.setRemoteAddr( req.getRemoteAddr() );
	        us.setFeedback( new UserFeedbackMessages() );
	        us.setNonceManager( new NonceManager() );
	        us.setStarted( new Date() );

	        l.info( us.getLogString() + "Starting session..." );
	        hs.setAttribute("userSession", us );
	    }
	    
	    return us;
	}

    /* ----------------------------------------------------------------- */
	// consistent format for the logs - this is a calculated value.
	public String getLogString() {
		return( remoteAddr +" "+ username +" " );
	}
	
	public NonceManager getNonceManager() { return nonceManager; }
	public void setNonceManager(NonceManager nonceManager) { this.nonceManager = nonceManager; }

	public UserFeedbackMessages getFeedback() { return feedback; }
	public void setFeedback(UserFeedbackMessages feedback) { this.feedback = feedback; }

	public Date getStarted() { return started; }
	public void setStarted(Date started) { this.started = started; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getRemoteAddr() { return remoteAddr; }
	public void setRemoteAddr(String remoteAddr) { this.remoteAddr = remoteAddr; }
		
}

