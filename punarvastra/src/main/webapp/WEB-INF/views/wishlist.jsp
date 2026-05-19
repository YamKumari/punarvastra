<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="extraCss" value="wishlist.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page">
<jsp:include page="/WEB-INF/templates/header.jsp"/>
<jsp:include page="/WEB-INF/templates/nav.jsp"/>
<main class="narrow">
  <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
  <h1>Wishlist</h1>
  <c:if test="${empty wishlistProducts}">
    <p class="muted">No saved items. <a href="${pageContext.request.contextPath}/products">Browse products</a></p>
  </c:if>
  <div class="wishlist-list">
    <c:forEach var="p" items="${wishlistProducts}">
      <article class="wishlist-row">
        <c:choose>
          <c:when test="${p.image == 'placeholder.png'}">
            <img src="${pageContext.request.contextPath}/static/images/placeholder.svg" alt="" class="wishlist-row__img"/>
          </c:when>
          <c:otherwise>
            <img src="${pageContext.request.contextPath}/photos/<c:out value='${p.image}'/>" alt="" class="wishlist-row__img"/>
          </c:otherwise>
        </c:choose>
        <div>
          <h2><a href="${pageContext.request.contextPath}/product?id=${p.id}"><c:out value="${p.title}"/></a></h2>
          <p>Rs. <c:out value="${p.price}"/></p>
        </div>
        <div class="wishlist-row__actions">
          <form method="post" action="${pageContext.request.contextPath}/wishlist">
            <input type="hidden" name="csrf" value="${sessionScope.csrfToken}"/>
            <input type="hidden" name="action" value="toCart"/>
            <input type="hidden" name="productId" value="${p.id}"/>
            <button type="submit" class="btn btn--primary">Move to cart</button>
          </form>
          <form method="post" action="${pageContext.request.contextPath}/wishlist" onsubmit="return confirm('Remove?');">
            <input type="hidden" name="csrf" value="${sessionScope.csrfToken}"/>
            <input type="hidden" name="action" value="remove"/>
            <input type="hidden" name="productId" value="${p.id}"/>
            <button type="submit" class="btn btn--ghost">Remove</button>
          </form>
        </div>
      </article>
    </c:forEach>
  </div>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
