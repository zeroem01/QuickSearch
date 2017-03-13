package dummyapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.cit.idm.util.UserFeedbackMessages;

/* -------------------------------------------------------------------------- */
public class LookupFbo {

private String netid;
private String nonce;
private String errorMessages = "";
private String successMessages = "";
protected final Log l = LogFactory.getLog(getClass()) ;

/* -------------------------------------------------------------------------- */
public String getNetid() { return this.netid; }
public void setNetid(String netid) { this.netid = netid; }

public String getNonce() { return this.nonce; }
public void setNonce(String nonce) { this.nonce = nonce; }

public String getErrorMessages() { return this.errorMessages; }
public void setErrorMessages(String message) { this.errorMessages = message; } 

public String getSuccessMessages() { return this.successMessages; }
public void setSuccessMessages(String message) { this.successMessages = message; } 

/* -------------------------------------------------------------------------- */
public void fromUserFeedbackMessages( UserFeedbackMessages msgs ) {
    setErrorMessages( msgs.getErrorMessages() );
    setSuccessMessages( msgs.getSuccessMessages() );
    msgs.clear();
}

/* -------------------------------------------------------------------------- */
public void dump() {
    l.info("NetID: " + netid);
    l.info("Nonce: " + nonce);
    l.info("errorMessages: " + errorMessages);
    l.info("successMessages: " + successMessages);
}

}
