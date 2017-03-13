package dummyapp ;

import java.util.List;

/* -------------------------------------------------------------------------- */
public class DisplayFbo {

private String netidLookedUp;
private List<LookupResult> lookupResults;

/* -------------------------------------------------------------------------- */

public String getNetidLookedUp() { return this.netidLookedUp; }
public void setNetidLookedUp( String netidLookedUp ) { this.netidLookedUp = netidLookedUp; }

public void setLookupResults( List<LookupResult> lookupResults ) { this.lookupResults = lookupResults; }
public List<LookupResult> getLookupResults() { return lookupResults; }

}
