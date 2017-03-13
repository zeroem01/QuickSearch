package dummyapp ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.springframework.validation.Validator ;
import org.springframework.validation.Errors ;

/* ------------------------------------------------------------------------- */
public class LookupVal implements Validator {

private final Log l = LogFactory.getLog(getClass());

/* ------------------------------------------------------------------------- */
public void validate (Object o, Errors e) {

    // this is just a sanity check on netid/serviceid/appid format - alphanumeric, 
    // not blank, slashes for serviceids, dashes for applicants.  
    LookupFbo lFbo = (LookupFbo)o ;

    String field = "netid";
    String netidList = lFbo.getNetid();

    if( netidList == null ) {
        e.rejectValue(field, field+".invalid", null, "The NetID can not be empty.") ;
    } else {
        netidList = netidList.trim();
        if( netidList.isEmpty() ) {
            e.rejectValue(field, field+".empty", null, "The NetID can not be empty.") ;
        } else {
        	try {
        		DisplayController.stringToSetOfNetids( netidList );
        	} catch( IllegalArgumentException ex ) {
        		l.warn("Invalid netid list (["+ netidList +"]: " + ex );
        		e.rejectValue(field, field+".malformed", null, "The value you entered doesn't look like a list of netids -- " + ex.getMessage() );
        	}
        }
    }
}

/* ------------------------------------------------------------------------- */
public boolean supports (@SuppressWarnings("rawtypes") Class clazz) {
    return clazz.equals(dummyapp.LookupFbo.class);
}

}

