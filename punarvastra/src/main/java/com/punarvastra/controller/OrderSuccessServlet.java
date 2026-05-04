package com.punarvastra.controller;

import com.punarvastra.entity.User;
import com.punarvastra.service.OrderService;
import com.punarvastra.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "OrderSuccessServlet", urlPatterns = {"/order-success"})
public class OrderSuccessServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Thank you");
        User u = SessionUtil.getCurrentUser(request.getSession(false));
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                var opt = orderService.getOrderForUser(id, u.getId());
                if (opt.isPresent()) {
                    request.setAttribute("order", opt.get());
                }
            } catch (NumberFormatException ignored) {
            } catch (java.sql.SQLException e) {
                throw new ServletException(e);
            }
        }
        ViewForwarder.forward(request, response, "order-success.jsp");
    }
}
