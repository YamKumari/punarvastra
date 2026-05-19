<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="site-nav" aria-label="Main">
    <button type="button" class="site-nav__toggle" id="navToggle" aria-controls="navPanel" aria-expanded="false">Menu</button>
    <ul class="site-nav__list" id="navPanel">
        <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/products">All products</a></li>
        <c:forEach var="cat" items="${navCategories}">
            <li><a href="${pageContext.request.contextPath}/products?category=${cat.slug}"><c:out value="${cat.name}"/></a></li>
        </c:forEach>
        <li><a href="${pageContext.request.contextPath}/about">About</a></li>
        <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
    </ul>
</nav>
