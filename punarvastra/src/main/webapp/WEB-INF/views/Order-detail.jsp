<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="checkout.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="narrow">
    <h1>Order #<c:out value="${order.id}"/></h1>
    <p class="pill"><c:out value="${order.status}"/></p>
    <p><strong>Total</strong> Rs. <c:out value="${order.totalAmount}"/></p>
    <p><strong>Payment</strong> <c:out value="${order.paymentMethod}"/></p>
    <p><strong>Phone</strong> <c:out value="${order.phone}"/></p>
    <p><strong>Address</strong> <c:out value="${order.shippingAddress}"/></p>
    <h2>Items</h2>
    <table class="data-table">
        <thead><tr><th>Product</th><th>Qty</th><th>Price</th></tr></thead>
        <tbody>
        <c:forEach var="it" items="${order.items}">
            <tr>
                <td><c:out value="${it.productTitle}"/></td>
                <td><c:out value="${it.quantity}"/></td>
                <td>Rs. <c:out value="${it.priceAtPurchase}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <p><a href="${pageContext.request.contextPath}/orders">← Back to orders</a></p>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
