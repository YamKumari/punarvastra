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
<main class="layout layout--sidebar">
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <aside class="filters">
        <h2>Filters</h2>
        <form method="get" action="${pageContext.request.contextPath}/products" class="filters__form">
            <c:if test="${not empty param.category}">
                <input type="hidden" name="category" value="<c:out value='${param.category}'/>"/>
            </c:if>
            <label>Size
                <select name="size">
                    <option value="">Any</option>
                    <option value="S" <c:if test="${filterSize eq 'S'}">selected</c:if>>S</option>
                    <option value="M" <c:if test="${filterSize eq 'M'}">selected</c:if>>M</option>
                    <option value="L" <c:if test="${filterSize eq 'L'}">selected</c:if>>L</option>
                    <option value="XL" <c:if test="${filterSize eq 'XL'}">selected</c:if>>XL</option>
                    <option value="Free" <c:if test="${filterSize eq 'Free'}">selected</c:if>>Free</option>
                </select>
            </label>
            <label>Min price (Rs.)
                <input type="number" name="minPrice" step="0.01" value="<c:out value='${filterMin}'/>"/>
            </label>
            <label>Max price (Rs.)
                <input type="number" name="maxPrice" step="0.01" value="<c:out value='${filterMax}'/>"/>
            </label>
            <label>Condition
                <select name="condition">
                    <option value="">Any</option>
                    <option value="New" <c:if test="${filterCondition eq 'New'}">selected</c:if>>New</option>
                    <option value="Like New" <c:if test="${filterCondition eq 'Like New'}">selected</c:if>>Like New</option>
                    <option value="Good" <c:if test="${filterCondition eq 'Good'}">selected</c:if>>Good</option>
                    <option value="Fair" <c:if test="${filterCondition eq 'Fair'}">selected</c:if>>Fair</option>
                </select>
            </label>
            <label>Sort
                <select name="sort">
                    <option value="newest" <c:if test="${empty filterSort or filterSort eq 'newest'}">selected</c:if>>Newest</option>
                    <option value="price_low" <c:if test="${filterSort eq 'price_low'}">selected</c:if>>Price low → high</option>
                    <option value="price_high" <c:if test="${filterSort eq 'price_high'}">selected</c:if>>Price high → low</option>
                </select>
            </label>
            <button type="submit" class="btn btn--primary">Apply</button>
            <a class="btn btn--ghost" href="${pageContext.request.contextPath}/products">Reset</a>
        </form>
    </aside>
    <section class="catalog">
        <h1 class="section__title">
            <c:choose>
                <c:when test="${not empty activeCategory}"><c:out value="${activeCategory.name}"/></c:when>
                <c:otherwise>All products</c:otherwise>
            </c:choose>
        </h1>
        <c:if test="${empty products}">
            <p class="muted">No items match your filters.</p>
        </c:if>
        <div class="product-grid">
            <c:forEach var="p" items="${products}">
                <c:set var="pc" value="${p}" scope="request"/>
                <jsp:include page="/WEB-INF/templates/product-tile.jsp"/>
            </c:forEach>
        </div>
    </section>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
