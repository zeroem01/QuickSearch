package dummyapp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/* -------------------------------------------------------------------------- */
public class ExceptionHandler implements HandlerExceptionResolver {

protected final Log l = LogFactory.getLog(getClass()) ;
private String errorView;

/* -------------------------------------------------------------------------- */
public ModelAndView resolveException (HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    Map<String, String> model = new HashMap<String, String>();

    l.error( "Unhandled exception: " + ex , ex );

    model.put( "errorDetail" , ex.getMessage() );
    return new ModelAndView( errorView , model );        
}

/* -------------------------------------------------------------------------- */
public void setErrorView (String errorView) { this.errorView = errorView; }
public String getErrorView () { return errorView; }

}
