<%@ include file="header.jsp" %>

<h2>Results of Lookup</h2>

<p><b>Looked up:</b> <c:out value="${dFbo.netidLookedUp}" /></p>

<% int rowcount = 0; %>

<table cellspacing="0" cellpadding="0">
  <tr>
    <th>NetID</th>
    <th>LDAP Attributes</th>
  </tr>
  <c:forEach var="res" items="${dFbo.lookupResults}">
    <tr class="row<%= (rowcount++)%2+1 %>">
        <td><c:out value="${res.netid}" /></td>
        <td>
            <c:forEach items="${res.attrs}" var="thisAttr">
                <b><c:out value="${thisAttr.key}"/></b>:
                <c:forEach items="${thisAttr.value}" var="thisValue">
                     <c:out value="${ thisValue }"/> 
                </c:forEach>
            <br/>
            </c:forEach>
        </td>
    </tr>
  </c:forEach>
</table>

<p><a href="@APP_BASEURL@lookup.html">Lookup another &gt;</a></p>

<%@ include file="footer.jsp" %>

