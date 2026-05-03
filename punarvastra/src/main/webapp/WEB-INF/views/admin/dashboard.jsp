<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Admin"/>
<c:set var="extraCss" value="admin.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page page--admin">
<header class="admin-top">
    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/products">Products</a>
    <a href="${pageContext.request.contextPath}/admin/products?status=PENDING">Pending listings</a>
    <a href="${pageContext.request.contextPath}/admin/categories">Categories</a>
    <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
    <a href="${pageContext.request.contextPath}/admin/users">Users</a>
    <a href="${pageContext.request.contextPath}/admin/inquiries">Inquiries</a>
    <a href="${pageContext.request.contextPath}/admin/analytics">Analytics</a>
    <a href="${pageContext.request.contextPath}/">Storefront</a>
</header>
<main class="admin-main">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>Dashboard</h1>
    <div class="stat-grid">
        <div class="stat-card"><span class="stat-card__label">Products (all)</span><span class="stat-card__value"><c:out value="${totalProducts}"/></span></div>
        <div class="stat-card"><span class="stat-card__label">Orders</span><span class="stat-card__value"><c:out value="${totalOrders}"/></span></div>
        <div class="stat-card"><span class="stat-card__label">Users</span><span class="stat-card__value"><c:out value="${totalUsers}"/></span></div>
        <div class="stat-card"><span class="stat-card__label">Revenue (month)</span><span class="stat-card__value">Rs. <c:out value="${revenueMonth}"/></span></div>
        <div class="stat-card stat-card--warn"><span class="stat-card__label">Pending listings</span><span class="stat-card__value"><c:out value="${pendingListings}"/></span></div>
        <div class="stat-card stat-card--warn"><span class="stat-card__label">Pending users</span><span class="stat-card__value"><c:out value="${pendingUsers}"/></span></div>
        <div class="stat-card"><span class="stat-card__label">Low stock (&lt;3)</span><span class="stat-card__value"><c:out value="${lowStock}"/></span></div>
    </div>
    <h2>Recent orders</h2>
    <table class="data-table">
        <thead><tr><th>ID</th><th>User</th><th>Total</th><th>Status</th></tr></thead>
        <tbody>
        <c:forEach var="o" items="${recentOrders}">
            <tr>
                <td><a href="${pageContext.request.contextPath}/admin/orders?id=${o.id}">#<c:out value="${o.id}"/></a></td>
                <td><c:out value="${o.userId}"/></td>
                <td>Rs. <c:out value="${o.totalAmount}"/></td>
                <td><c:out value="${o.status}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
</body>
</html>
