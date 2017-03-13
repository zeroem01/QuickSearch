package dummyapp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.List;
import java.util.TreeSet;

public class LookupResult {

	private String netid = "";
	private Map<String,List<String>> attrs = null;
	
	public String getNetid() { return netid; }
	public void setNetid( String netid ) { this.netid = netid; }
	
	public Map<String,List<String>> getAttrs() { return this.attrs; }
	public void setAttrs( Map<String,List<String>> newAttrs ) { this.attrs = newAttrs; }
	
	public boolean isExists() { return( attrs != null ); }
	
	public int nattrs() {
		return( attrs==null ? 0 : attrs.size() );
	}
	
	public String toString() {
		String s = netid + "::nattrs:" + nattrs() ;
		return s;
	}
	
}
