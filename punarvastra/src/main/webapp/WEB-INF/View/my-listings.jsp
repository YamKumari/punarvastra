<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="profile.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="narrow">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>My listings</h1>
    <p><a class="btn btn--primary" href="${pageContext.request.contextPath}/sell">+ New listing</a></p>
    <table class="data-table">
        <thead><tr><th>Title</th><th>Status</th><th>Updated</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="p" items="${myProducts}">
            <tr>
                <td><c:out value="${p.title}"/></td>
                <td><span class="pill pill--${p.listingStatus}"><c:out value="${p.listingStatus}"/></span></td>
                <td><c:out value="${p.updatedAt}"/></td>
                <td>
                    <c:if test="${p.listingStatus eq 'REJECTED'}">
                        <form method="post" action="${pageContext.request.contextPath}/my-listings" style="display:inline">
                            <input type="hidden" name="csrf" value="${csrf}"/>
                            <input type="hidden" name="action" value="resubmit"/>
                            <input type="hidden" name="productId" value="${p.id}"/>
                            <button type="submit" class="btn btn--sm btn--primary">Resubmit for review</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
