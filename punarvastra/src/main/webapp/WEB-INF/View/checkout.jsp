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
  <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
  <h1>Checkout</h1>
  <form method="post" action="${pageContext.request.contextPath}/order" class="stack-form">
    <input type="hidden" name="csrf" value="${csrf}"/>
    <input type="hidden" name="action" value="place"/>
    <label>Shipping address
      <textarea name="shippingAddress" rows="4" required><c:out value="${profileAddress}"/></textarea>
    </label>
    <label>Phone
      <input type="text" name="phone" value="<c:out value='${profilePhone}'/>" required maxlength="15"/>
    </label>
    <label>Payment
      <select name="paymentMethod">
        <option value="COD">Cash on delivery</option>
        <option value="ESEWA">eSewa (manual confirmation)</option>
      </select>
    </label>
    <button type="submit" class="btn btn--primary btn--lg">Place order</button>
  </form>
</main>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="${pageContext.request.contextPath}/static/js/menu-toggle.js"></script>
</body>
</html>
