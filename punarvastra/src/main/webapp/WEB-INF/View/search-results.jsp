<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="product-list.css"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/templates/head.jsp"/>
</head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main>
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1 class="section__title">Search<c:if test="${not empty searchQ}">: <c:out value="${searchQ}"/></c:if></h1>
    <c:if test="${empty products}">
        <p class="muted">No products found.</p>
    </c:if>
    <div class="product-grid">
        <c:forEach var="p" items="${products}">
            <c:set var="pc" value="${p}" scope="request"/>
            <jsp:include page="/WEB-INF/templates/product-tile.jsp"/>
        </c:forEach>
    </div>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
