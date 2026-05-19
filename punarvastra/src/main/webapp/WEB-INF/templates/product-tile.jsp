<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="p" value="${requestScope.pc}"/>

<article class="product-card animate-rise">
    <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="product-card__link">
        <div class="product-card__img-wrap">
            <c:choose>
                <c:when test="${p.image == 'placeholder.png' || empty p.image}">
                    <img src="${pageContext.request.contextPath}/static/images/placeholder.svg"
                         alt="${p.title}"
                         class="product-card__img"/>
                </c:when>
                <c:otherwise>
                    <!-- New path: uses ImageServlet -->
                    <img src="${pageContext.request.contextPath}/uploads/${p.image}"
                         alt="${p.title}"
                         class="product-card__img"
                         onerror="this.src='${pageContext.request.contextPath}/static/images/placeholder.svg'"/>
                </c:otherwise>
            </c:choose>
        </div>
        <h3 class="product-card__title"><c:out value="${p.title}"/></h3>
        <p class="product-card__meta"><c:out value="${p.categoryName}"/> · <c:out value="${p.size}"/></p>
        <p class="product-card__price">Rs. <c:out value="${p.price}"/></p>
    </a>
    <div class="product-card__actions">
        <c:if test="${not empty sessionScope.currentUser}">
            <form action="${pageContext.request.contextPath}/cart" method="post" class="inline-form">
                <input type="hidden" name="csrf" value="${sessionScope.csrfToken}"/>
                <input type="hidden" name="action" value="add"/>
                <input type="hidden" name="productId" value="${p.id}"/>
                <input type="hidden" name="quantity" value="1"/>
                <button type="submit" class="btn btn--sm btn--primary">Cart</button>
            </form>
            <form action="${pageContext.request.contextPath}/wishlist" method="post" class="inline-form">
                <input type="hidden" name="csrf" value="${sessionScope.csrfToken}"/>
                <input type="hidden" name="action" value="add"/>
                <input type="hidden" name="productId" value="${p.id}"/>
                <button type="submit" class="btn btn--sm btn--ghost">Wishlist</button>
            </form>
        </c:if>
        <c:if test="${empty sessionScope.currentUser}">
            <a class="btn btn--sm btn--primary" href="${pageContext.request.contextPath}/login">Login</a>
        </c:if>
    </div>
</article>