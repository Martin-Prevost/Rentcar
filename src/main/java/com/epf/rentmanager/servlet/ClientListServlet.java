package com.epf.rentmanager.servlet;

import com.epf.rentmanager.service.ClientService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users")
public class ClientListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ClientService clientService = ClientService.getInstance();
        try {
            request.setAttribute("clients", clientService.findAll());
        } catch (Exception e) {
            throw new ServletException();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/list.jsp").forward(request, response);
    }
}
