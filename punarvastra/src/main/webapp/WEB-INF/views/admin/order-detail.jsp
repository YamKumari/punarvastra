<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Order"/>
<c:set var="extraCss" value="admin.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page page--admin">
<header class="admin-top"><a href="${pageContext.request.contextPath}/admin/orders">← Orders</a></header>
<main class="admin-main">
  <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
  <h1>Order #<c:out value="${order.id}"/></h1>
  <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="stack-form">
    <input type="hidden" name="csrf" value="${csrf}"/>
    <input type="hidden" name="orderId" value="${order.id}"/>
    <label>Status
      <select name="status">
        <option value="PENDING" <c:if test="${order.status eq 'PENDING'}">selected</c:if>>PENDING</option>
        <option value="CONFIRMED" <c:if test="${order.status eq 'CONFIRMED'}">selected</c:if>>CONFIRMED</option>
        <option value="SHIPPED" <c:if test="${order.status eq 'SHIPPED'}">selected</c:if>>SHIPPED</option>
        <option value="DELIVERED" <c:if test="${order.status eq 'DELIVERED'}">selected</c:if>>DELIVERED</option>
        <option value="CANCELLED" <c:if test="${order.status eq 'CANCELLED'}">selected</c:if>>CANCELLED</option>
      </select>
    </label>
    <button type="submit" class="btn btn--primary">Update status</button>
  </form>
  <p>Total Rs. <c:out value="${order.totalAmount}"/> · Payment <c:out value="${order.paymentMethod}"/></p>
  <table class="data-table">
    <thead><tr><th>Image</th><th>Product</th><th>Qty</th><th>Price</th></tr></thead>
    <tbody>
    <c:forEach var="it" items="${order.items}">
      <tr>
        <td style="width:72px">
          <c:set var="img" value="${empty it.productImage ? 'static/images/placeholder.svg' : 'photos/'.concat(it.productImage)}"/>
          <img src="${pageContext.request.contextPath}/${img}" alt="" style="width:56px;height:56px;object-fit:cover;border-radius:10px;border:1px solid #e5e0d8"/>
        </td>
        <td><c:out value="${it.productTitle}"/></td>
        <td><c:out value="${it.quantity}"/></td>
        <td>Rs. <c:out value="${it.priceAtPurchase}"/></td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</main>
</body>
</html>
