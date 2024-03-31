package com.epf.rentmanager.servlet.users;


import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/users/edit")
public class ClientEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Autowired
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("client", clientService.findById(
                    Long.parseLong(request.getParameter("id"))));
        } catch (ServiceException e) {
            throw new ServletException();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String prenom = request.getParameter("last_name");
            String firstName = request.getParameter("first_name");
            String email = request.getParameter("email");
            LocalDate dateNassance = java.time.LocalDate.parse(request.getParameter("birth"));

            if (prenom.isEmpty() || firstName.isEmpty())
                throw new ServletException("Nom et prénom ne doivent pas être vide");
            if (prenom.length() < 3 || firstName.length() < 3)
                throw new ServletException("Nom et prénom doivent faire au moins 3 caractères");
            if (dateNassance == null || dateNassance.isAfter(java.time.LocalDate.now().minusYears(18)))
                throw new ServletException("Date de naissance invalide");
            String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if (!email.matches(regex))
                throw new ServletException("Email invalide");

            clientService.update(new Client(prenom, firstName, email, dateNassance));
        } catch (ServiceException e) {
            throw new ServletException();
        }
        response.sendRedirect(request.getContextPath() + "/users");
    }
}
