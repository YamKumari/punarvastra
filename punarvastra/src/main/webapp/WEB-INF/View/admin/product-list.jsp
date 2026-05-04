<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Admin products"/>
<c:set var="extraCss" value="admin.css"/>
<!DOCTYPE html>
<html lang="en">
<head><jsp:include page="/WEB-INF/templates/head.jsp"/></head>
<body class="page page--admin">
<header class="admin-top">
  <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
  <a href="${pageContext.request.contextPath}/admin/products?status=PENDING"><strong>Pending</strong></a>
  <a href="${pageContext.request.contextPath}/admin/products?status=APPROVED">Approved</a>
  <a href="${pageContext.request.contextPath}/admin/products?status=REJECTED">Rejected</a>
  <a href="${pageContext.request.contextPath}/admin/products?status=ALL">All</a>
</header>
<main class="admin-main">
  <jsp:include page="/WEB-INF/templates/flash-messages.jsp"/>
  <h1>Products <span class="pill"><c:out value="${listStatus}"/></span></h1>

  <table class="data-table">
    <thead><tr><th>ID</th><th>Title</th><th>Price</th><th>Status</th><th>Actions</th></tr></thead>
    <tbody>
    <c:forEach var="p" items="${products}">
      <tr>
        <td><c:out value="${p.id}"/></td>
        <td><c:out value="${p.title}"/></td>
        <td><c:out value="${p.price}"/></td>
        <td><c:out value="${p.listingStatus}"/></td>
        <td class="nowrap">
          <c:if test="${p.listingStatus eq 'PENDING'}">
            <form method="post" action="${pageContext.request.contextPath}/admin/products" class="inline-form">
              <input type="hidden" name="csrf" value="${csrf}"/>
              <input type="hidden" name="action" value="approve"/>
              <input type="hidden" name="productId" value="${p.id}"/>
              <button type="submit" class="btn btn--sm btn--primary">Approve</button>
            </form>
            <form method="post" action="${pageContext.request.contextPath}/admin/products" class="inline-form">
              <input type="hidden" name="csrf" value="${csrf}"/>
              <input type="hidden" name="action" value="reject"/>
              <input type="hidden" name="productId" value="${p.id}"/>
              <button type="submit" class="btn btn--sm btn--danger">Reject</button>
            </form>
          </c:if>
          <a class="btn btn--sm btn--ghost" href="${pageContext.request.contextPath}/admin/products?status=${listStatus}&amp;edit=${p.id}">Edit</a>
          <form method="post" action="${pageContext.request.contextPath}/admin/products" class="inline-form" onsubmit="return confirm('Delete product?');">
            <input type="hidden" name="csrf" value="${csrf}"/>
            <input type="hidden" name="action" value="delete"/>
            <input type="hidden" name="productId" value="${p.id}"/>
            <button type="submit" class="btn btn--sm btn--danger">Delete</button>
          </form>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  <h2><c:choose><c:when test="${not empty editProduct}">Edit</c:when><c:otherwise>Add</c:otherwise></c:choose> Product</h2>

  <form method="post" action="${pageContext.request.contextPath}/admin/products"
        enctype="multipart/form-data" class="stack-form admin-form">
    <input type="hidden" name="csrf" value="${csrf}"/>
    <input type="hidden" name="action" value="save"/>

    <c:if test="${editProduct.id ne null}">
      <input type="hidden" name="id" value="${editProduct.id}"/>
      <input type="hidden" name="existingImage" value="<c:out value='${editProduct.image}'/>"/>
    </c:if>

    <label>Title <input type="text" name="title" required value="<c:out value='${editProduct.title}'/>"/></label>
    <label>Description <textarea name="description" rows="3" required><c:out value="${editProduct.description}"/></textarea></label>
    <label>Price <input type="number" name="price" step="0.01" required value="<c:out value='${editProduct.price}'/>"/></label>
    <label>Size <input type="text" name="size" required value="<c:out value='${editProduct.size}'/>"/></label>
    <label>Condition <input type="text" name="productCondition" required value="<c:out value='${editProduct.productCondition}'/>"/></label>
    <label>Brand <input type="text" name="brand" value="<c:out value='${editProduct.brand}'/>"/></label>
    <label>Stock <input type="number" name="stock" required value="<c:out value='${editProduct.stock}'/>"/></label>

    <label>Listing status
      <select name="listingStatus">
        <option value="PENDING" <c:if test="${editProduct.id ne null and editProduct.listingStatus eq 'PENDING'}">selected</c:if>>PENDING</option>
        <option value="APPROVED" <c:if test="${editProduct.id eq null or editProduct.listingStatus eq 'APPROVED'}">selected</c:if>>APPROVED</option>
        <option value="REJECTED" <c:if test="${editProduct.id ne null and editProduct.listingStatus eq 'REJECTED'}">selected</c:if>>REJECTED</option>
      </select>
    </label>

    <label>Category
      <select name="categoryId">
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.id}" <c:if test="${not empty editProduct and editProduct.categoryId eq cat.id}">selected</c:if>>
            <c:out value="${cat.name}"/>
          </option>
        </c:forEach>
      </select>
    </label>

    <!-- Image Upload -->
    <label>Image (optional)
      <input type="file" name="image" accept=".jpg,.jpeg,.png"/>
    </label>

    <!-- Show current image when editing -->
    <c:if test="${not empty editProduct and editProduct.image ne 'placeholder.png'}">
      <div style="margin: 10px 0;">
        <p>Current Image:</p>
        <img src="${pageContext.request.contextPath}/uploads/${editProduct.image}"
             alt="Current" style="max-width: 200px; border: 1px solid #ddd;"/>
      </div>
    </c:if>

    <button type="submit" class="btn btn--primary">Save Product</button>
  </form>
</main>
</body>
</html>