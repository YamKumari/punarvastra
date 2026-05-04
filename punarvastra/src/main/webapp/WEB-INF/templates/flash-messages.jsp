<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${not empty flashSuccess}">
  <div class="flash flash--success" role="status"><c:out value="${flashSuccess}"/></div>
</c:if>
<c:if test="${not empty flashError}">
  <div class="flash flash--error" role="alert"><c:out value="${flashError}"/></div>
</c:if>
