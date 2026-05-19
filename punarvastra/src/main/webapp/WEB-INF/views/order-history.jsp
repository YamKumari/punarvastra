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
    <h1>Order history</h1>
    <form method="get" class="inline-row" action="${pageContext.request.contextPath}/orders">
        <label>Status <select name="status" onchange="this.form.submit()">
            <option value="ALL" <c:if test="${empty filterStatus or filterStatus eq 'ALL'}">selected</c:if>>All</option>
            <option value="PENDING" <c:if test="${filterStatus eq 'PENDING'}">selected</c:if>>Pending</option>
            <option value="CONFIRMED" <c:if test="${filterStatus eq 'CONFIRMED'}">selected</c:if>>Confirmed</option>
            <option value="SHIPPED" <c:if test="${filterStatus eq 'SHIPPED'}">selected</c:if>>Shipped</option>
            <option value="DELIVERED" <c:if test="${filterStatus eq 'DELIVERED'}">selected</c:if>>Delivered</option>
            <option value="CANCELLED" <c:if test="${filterStatus eq 'CANCELLED'}">selected</c:if>>Cancelled</option>
        </select></label>
    </form>
    <table class="data-table">
        <thead><tr><th>ID</th><th>Date</th><th>Total</th><th>Status</th></tr></thead>
        <tbody>
        <c:forEach var="o" items="${orders}">
            <tr>
                <td><a href="${pageContext.request.contextPath}/order?id=${o.id}">#<c:out value="${o.id}"/></a></td>
                <td><c:out value="${o.createdAt}"/></td>
                <td>Rs. <c:out value="${o.totalAmount}"/></td>
                <td><span class="pill"><c:out value="${o.status}"/></span></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
