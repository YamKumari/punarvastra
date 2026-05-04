<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="product-detail.css"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/templates/head.jsp"/>
</head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="detail">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <div class="detail__grid">
        <div class="detail__media animate-fade">
            <c:choose>
                <c:when test="${product.image == 'placeholder.png' || empty product.image}">
                    <img src="${pageContext.request.contextPath}/static/images/placeholder.svg"
                         alt="${product.title}"
                         class="detail__img"/>
                </c:when>
                <c:otherwise>
                    <!-- Updated to use new uploads folder -->
                    <img src="${pageContext.request.contextPath}/uploads/<c:out value='${product.image}'/>"
                         alt="${product.title}"
                         class="detail__img"
                         onerror="this.src='${pageContext.request.contextPath}/static/images/placeholder.svg'"/>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="detail__info">
            <p class="eyebrow"><c:out value="${product.categoryName}"/></p>
            <h1><c:out value="${product.title}"/></h1>
            <p class="detail__price">Rs. <c:out value="${product.price}"/></p>
            <ul class="detail__facts">
                <li><strong>Size</strong> <c:out value="${product.size}"/></li>
                <li><strong>Condition</strong> <c:out value="${product.productCondition}"/></li>
                <li><strong>Brand</strong> <c:out value="${product.brand}"/></li>
                <li><strong>Stock</strong> <c:out value="${product.stock}"/></li>
            </ul>
            <p class="detail__desc"><c:out value="${product.description}"/></p>
            <div class="detail__actions">
                <c:if test="${loggedIn}">
                    <form action="${pageContext.request.contextPath}/cart" method="post" class="stack-form">
                        <input type="hidden" name="csrf" value="${sessionScope.csrfToken}"/>
                        <input type="hidden" name="action" value="add"/>
                        <input type="hidden" name="productId" value="${product.id}"/>
                        <input type="hidden" name="redirect" value="/product?id=${product.id}"/>
                        <label>Qty <input type="number" name="quantity" value="1" min="1" max="${product.stock}" class="qty-input"/></label>
                        <button type="submit" class="btn btn--primary btn--lg">Add to cart</button>
                    </form>
                    <c:choose>
                        <c:when test="${inWishlist}">
                            <p class="muted">Already in your wishlist - open Wishlist in the header.</p>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/wishlist" method="post" class="stack-form">
                                <input type="hidden" name="csrf" value="${sessionScope.csrfToken}"/>
                                <input type="hidden" name="action" value="add"/>
                                <input type="hidden" name="productId" value="${product.id}"/>
                                <input type="hidden" name="redirect" value="/product?id=${product.id}"/>
                                <button type="submit" class="btn btn--ghost btn--lg">Add to wishlist</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${not loggedIn}">
                    <a class="btn btn--primary btn--lg" href="${pageContext.request.contextPath}/login">Login to add to cart or wishlist</a>
                </c:if>
            </div>
        </div>
    </div>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
<script src="${pageContext.request.contextPath}/static/js/qty-control.js"></script>
</body>
</html>