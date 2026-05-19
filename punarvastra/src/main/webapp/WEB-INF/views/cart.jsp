<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="extraCss" value="cart.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="narrow">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <h1>Your cart</h1>
    <c:if test="${empty cart}">
        <p class="muted">Cart is empty. <a href="${pageContext.request.contextPath}/products">Continue shopping</a></p>
    </c:if>
    <c:if test="${not empty cart}">
        <table class="data-table">
            <thead><tr><th>Item</th><th>Price</th><th>Qty</th><th>Line</th><th></th></tr></thead>
            <tbody>
            <c:forEach var="line" items="${cart}">
                <tr>
                    <td><c:out value="${line.product.title}"/></td>
                    <td>Rs. <c:out value="${line.product.price}"/></td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/cart" class="inline-row">
                            <input type="hidden" name="csrf" value="${sessionScope.csrfToken}"/>
                            <input type="hidden" name="action" value="update"/>
                            <input type="hidden" name="productId" value="${line.productId}"/>
                            <input type="number" name="quantity" value="${line.quantity}" min="0" max="${line.product.stock}" class="qty-input"/>
                            <button type="submit" class="btn btn--sm">Update</button>
                        </form>
                    </td>
                    <td>Rs. <fmt:formatNumber value="${line.lineTotal}" maxFractionDigits="2"/></td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/cart" onsubmit="return confirm('Remove this item?');">
                            <input type="hidden" name="csrf" value="${sessionScope.csrfToken}"/>
                            <input type="hidden" name="action" value="remove"/>
                            <input type="hidden" name="productId" value="${line.productId}"/>
                            <button type="submit" class="btn btn--sm btn--danger">Remove</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <p class="cart-total"><strong>Estimated total:</strong> Rs. <fmt:formatNumber value="${cartTotal}" maxFractionDigits="2"/></p>
        <a class="btn btn--primary btn--lg" href="${pageContext.request.contextPath}/checkout">Checkout</a>
    </c:if>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
