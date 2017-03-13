<%@ include file="header.jsp" %>

<h2>Lookup a NetID</h2>

<c:if test="${ not empty lFbo.errorMessages }">
    <div class="message-error">
        <p><c:out value="${lFbo.errorMessages}"/></p>
    </div>
</c:if>

<c:if test="${ not empty lFbo.successMessages }">
    <div class="message-success">
        <p><c:out value="${lFbo.successMessages}"/></p>
    </div>
</c:if>

<form action="" method="post">

<fieldset class="row1">
  <spring:bind path="lFbo.netid">
    <div class="form-pair">
        <div class="form-item">
            <label for="<c:out value="${status.expression}" />">List of NetIDs</label>
        </div>
        <div class="form-value">
            <input type="text" class="input-text" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
            <p class="form-error"><c:out value="${status.errorMessage}"/></p>
        </div>
    </div>
  </spring:bind>
</fieldset>

<fieldset class="form-submit">
  <spring:bind path="lFbo.nonce">
    <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}" />" />
  </spring:bind>
    <div class="form-submit-buttons">
        <input class="input-submit" value="submit" type="submit">
    </div>
</fieldset>

</form>

<%@ include file="footer.jsp" %>

