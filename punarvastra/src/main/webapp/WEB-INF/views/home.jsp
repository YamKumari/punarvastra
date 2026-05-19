<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="home.css"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/WEB-INF/templates/head.jsp"/>
</head>
<body class="page page--home">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main>
    <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
    <section class="hero animate-fade">
        <div class="hero__content">
            <h1 class="hero__title">Renewed clothing for Nepal</h1>
            <p class="hero__lead">Curated thrift finds from Kathmandu valley - fair prices, lower footprint.</p>
            <a class="btn btn--primary btn--lg" href="${pageContext.request.contextPath}/products">Browse catalogue</a>
        </div>
    </section>
    <section class="section">
        <h2 class="section__title">Featured picks</h2>
        <div class="product-grid">
            <c:forEach var="p" items="${featured}">
                <c:set var="pc" value="${p}" scope="request"/>
                <jsp:include page="/WEB-INF/templates/product-tile.jsp"/>
            </c:forEach>
        </div>
    </section>
    <section class="section section--alt">
        <h2 class="section__title">Why thrift?</h2>
        <div class="why-grid">
            <div class="why-card"><h3>Less waste</h3><p>Extend garment life and keep textiles out of landfill.</p></div>
            <div class="why-card"><h3>Fair NPR</h3><p>Accessible style for students and young professionals.</p></div>
            <div class="why-card"><h3>Local sellers</h3><p>List pieces you no longer wear - admin checks every listing.</p></div>
        </div>
    </section>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
