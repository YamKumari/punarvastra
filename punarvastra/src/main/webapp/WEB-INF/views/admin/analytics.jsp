<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Analytics"/>
<c:set var="extraCss" value="admin.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page page--admin">
<header class="admin-top"><a href="${pageContext.request.contextPath}/admin/dashboard">← Dashboard</a></header>
<main class="admin-main">
    <h1>Analytics</h1>
    <p>Low-stock SKUs (approved, stock &lt; 3): <strong><c:out value="${lowStockCount}"/></strong></p>
    <h2>Top products (units sold)</h2>
    <table class="data-table">
        <thead><tr><th>Title</th><th>Units</th></tr></thead>
        <tbody>
        <c:forEach var="row" items="${topProducts}">
            <tr><td><c:out value="${row.title}"/></td><td><c:out value="${row.units}"/></td></tr>
        </c:forEach>
        </tbody>
    </table>
    <h2>Revenue by month</h2>
    <table class="data-table">
        <thead><tr><th>Month</th><th>Revenue (NPR)</th></tr></thead>
        <tbody>
        <c:forEach var="row" items="${revenueMonths}">
            <tr><td><c:out value="${row.month}"/></td><td><c:out value="${row.revenue}"/></td></tr>
        </c:forEach>
        </tbody>
    </table>
    <h2>Orders by status</h2>
    <table class="data-table">
        <thead><tr><th>Status</th><th>Count</th></tr></thead>
        <tbody>
        <c:forEach var="row" items="${ordersByStatus}">
            <tr><td><c:out value="${row.status}"/></td><td><c:out value="${row.count}"/></td></tr>
        </c:forEach>
        </tbody>
    </table>
</main>
</body>
</html>
