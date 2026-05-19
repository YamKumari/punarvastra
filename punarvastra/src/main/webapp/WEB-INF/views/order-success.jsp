<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="narrow center-text">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>Thank you!</h1>
    <c:if test="${not empty order}">
        <p>Your order <strong>#<c:out value="${order.id}"/></strong> is <c:out value="${order.status}"/>.</p>
        <p>Total paid (estimate): Rs. <c:out value="${order.totalAmount}"/></p>
    </c:if>
    <a class="btn btn--primary" href="${pageContext.request.contextPath}/orders">View orders</a>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
