package com.epf.rentmanager.servlet.users;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/users/create")
public class ClientCreateServlet extends HttpServlet {
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
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        try {
            clientService.create(new Client(prenom, firstName, email, dateNassance));
        } catch (ServiceException e) {
            throw new ServletException();
        }
        response.sendRedirect(request.getContextPath() + "/users");
    }
}
