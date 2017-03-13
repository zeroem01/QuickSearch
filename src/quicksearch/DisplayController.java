package dummyapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.NamingEnumeration;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.cit.idm.datastore.DirectoryServer;
import edu.cornell.cit.idm.servers.NotFoundException;

import edu.cornell.cit.idm.util.RecoverableException;

/* -------------------------------------------------------------------------- */
public class DisplayController extends AbstractController {

private DirectoryServer ldap;
public DirectoryServer getLdap() { return ldap; }
public void setLdap( DirectoryServer ds ) { this.ldap = ds; }

protected final Log l = LogFactory.getLog(getClass()) ;

/* -------------------------------------------------------------------------- */
protected ModelAndView handleRequestInternal( HttpServletRequest req , HttpServletResponse resp ) throws ServletException {

DisplayFbo dFbo = new DisplayFbo();
ModelAndView mav;

UserSession sess = UserSession.getInstance(req);
String userLogString = sess.getLogString();

try {

	HttpSession hs = req.getSession() ;
	String netidListToLookup = (String)hs.getAttribute("netidToLookup"); 
			
	if ( netidListToLookup == null || netidListToLookup.isEmpty() )
		throw new RecoverableException("Your session has expired. Please try again.");
	
	dFbo.setNetidLookedUp( netidListToLookup );
	
	Set<String> setOfNetids = stringToSetOfNetids( netidListToLookup );
	
    l.info( userLogString + "Look up NetID list ["+ netidListToLookup +"] ("+ setOfNetids.size() +" members)");	
	
	List<LookupResult> results = new ArrayList<LookupResult>();
	
	ldap.persistentConnect();
	
	for( String netidToLookup : setOfNetids ) {

		LookupResult result = new LookupResult();
		result.setNetid( netidToLookup );
		
		try {
            Attributes attrs = ldap.lookupByNetidToDirectorySearchResult(netidToLookup).getAttributes();
            Map<String,List<String>> nvp = new TreeMap<String,List<String>>();
            NamingEnumeration<String> allAttrs = attrs.getIDs();
            while( allAttrs.hasMore() ) {
                String thisKey = allAttrs.next();
                List<String> thisValues = ldap.attrToListOfStrings(attrs,thisKey);
                if( thisValues.size() > 0 ) {
                    nvp.put( thisKey , thisValues );
                }
            }
			result.setAttrs( nvp );
		} catch (NotFoundException nfe) {;}

		l.info( userLogString + " lookup - " + result );
		results.add(result);
	}
	
	dFbo.setLookupResults( results );
	
    mav = new ModelAndView("display", "dFbo", dFbo);
	
} catch (RecoverableException ex) {
	l.warn( userLogString +"Recoverable exception: " + ex );
	sess.getFeedback().setErrorMessage( ex.getMessage() ); 
	mav = new ModelAndView( new RedirectView("lookup.html") );
    
} catch (Exception ex) {
    // these shouldn't ever come up in normal operation. They would mean that some server 
    // is down, or something along those lines.
    l.error( userLogString + "Unhandled exception: " + ex , ex );
    
    throw new ServletException( "An internal error has occurred, please try again in a few minutes." );
} finally {
	try {
		ldap.persistentClose();
	} catch( Exception e ) {;}
}
    
return (mav) ;
}

protected static final String listOfDelimiters = ", ;\t\n\r";
protected static final String netidPattern = "[a-z0-9/_-]+"; 
// i made it public so that it can also be used in the validator.
public static Set<String> stringToSetOfNetids( String putativeListOfNetids ) throws IllegalArgumentException {
	Set<String> lowerCaseNetids = new HashSet<String>();
	
	StringBuffer buf = new StringBuffer();
	int pos = 0;
	while( pos < putativeListOfNetids.length() ) {
		final char c = putativeListOfNetids.charAt(pos);
		
		boolean bufferIsReadyToCheck = false;
		if( listOfDelimiters.indexOf(c) >= 0 ) {
			bufferIsReadyToCheck = true;
		} else if( pos == putativeListOfNetids.length()-1 ) {
			buf.append(c);
			bufferIsReadyToCheck = true;
		}

		if( bufferIsReadyToCheck ) {
			if( buf.length() == 0 ) { 
				// ok, ignore empty entries
			} else {
				String thisPutativeNetid = buf.toString().toLowerCase().trim();
				if( thisPutativeNetid.matches(netidPattern) ) {
					lowerCaseNetids.add( thisPutativeNetid );
				} else {
					throw new IllegalArgumentException( "The netid at position "+ lowerCaseNetids.size() +", ["+ thisPutativeNetid +"], looks malformed." );
				}
			}
			// clear out the buffer so we can accumulate the next thing in the list.
			buf.delete( 0 , buf.length() );
			
		} else {
			// didn't find a delimiter yet, keep accumulating.
			buf.append(c);
		}
		
		++pos;
	}
	return lowerCaseNetids;
}

}
