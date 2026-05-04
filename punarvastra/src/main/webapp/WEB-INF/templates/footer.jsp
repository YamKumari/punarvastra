<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<footer class="site-footer">
    <div class="site-footer__grid">
        <div>
            <strong>PunarVastra</strong>
            <p class="muted">Second-hand clothing marketplace for Nepal - kinder to wallets and the planet.</p>
        </div>
        <div>
            <strong>Shop</strong>
            <ul>
                <li><a href="${pageContext.request.contextPath}/products">Browse</a></li>
                <li><a href="${pageContext.request.contextPath}/sell">Sell with us</a></li>
            </ul>
        </div>
        <div>
            <strong>Support</strong>
            <ul>
                <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                <li><a href="${pageContext.request.contextPath}/about">Our story</a></li>
            </ul>
        </div>
    </div>
    <p class="site-footer__copy">&copy; <c:out value="${pageContext.request.serverName}"/> - coursework demo</p>
</footer>
