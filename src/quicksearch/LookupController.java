package dummyapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.cornell.cit.idm.util.RecoverableException;

// a draft demonstration/skeleton spring app. 
// What it does is: you give it a netid and it looks up the person's name and directory type (affiliation).

/* -------------------------------------------------------------------------- */
public class LookupController extends SimpleFormController {

protected final Log l = LogFactory.getLog( getClass() );

/* -------------------------------------------------------------------------- */
protected Object formBackingObject( HttpServletRequest req ) throws ServletException {

    LookupFbo lFbo = new LookupFbo();
    
    UserSession sess = UserSession.getInstance(req);
    String userLogString = sess.getLogString();

    try {

        lFbo.setNonce( sess.getNonceManager().newToken() );

        // move any feedback for the user out of the session and into the page he's about to see.
        lFbo.fromUserFeedbackMessages( sess.getFeedback() );

    } catch( Exception ex ) {
        // these shouldn't ever come up in normal operation. 
        l.error( userLogString + "Unhandled exception: " + ex );
        throw new ServletException( ex );
    }

    return( lFbo );
}//formBackingObject()

/* -------------------------------------------------------------------------- */
public ModelAndView onSubmit (HttpServletRequest req, HttpServletResponse resp, 
        Object o, BindException errors) throws ServletException, Exception {

    LookupFbo lFbo = (LookupFbo)o ;
    ModelAndView mav;
    UserSession sess = UserSession.getInstance(req);
    String userLogString = sess.getLogString();

    try {

        String netidToLookup = lFbo.getNetid().trim().toLowerCase();

        // this throws a RecoverableException if the token is invalid.
        sess.getNonceManager().attemptToUseToken( lFbo.getNonce() ); 

        // now it's ok to go to step 2
    	HttpSession hs = req.getSession() ;
        hs.setAttribute("netidToLookup", netidToLookup);
        mav = new ModelAndView( new RedirectView("display.html") );

    } catch (RecoverableException ex) {
        l.warn( userLogString + "Recoverable exception: " + ex );
        // put that error into the http session so it can be displayed later.
        sess.getFeedback().setErrorMessage( ex.getMessage() );
        // display the lookup form again instead of going to step 2.
        mav = new ModelAndView( "lookup" , "lFbo" , this.formBackingObject(req) );
        
    } catch (Exception ex) {
        l.error( userLogString + "Unhandled exception: " + ex );
        throw new ServletException( ex );
    }

    return( mav );
}//onSubmit()

}
