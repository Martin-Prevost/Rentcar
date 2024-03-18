package com.epf.rentmanager.servlet;

import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("clients", clientService.findAll());
            request.setAttribute("vehicles", vehicleService.findAll());
        } catch (Exception e) {
            throw new ServletException();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            reservationService.create(new Reservation(
                    Long.parseLong(request.getParameter("client")),
                    Long.parseLong(request.getParameter("car")),
                    java.time.LocalDate.parse(request.getParameter("begin")),
                    java.time.LocalDate.parse(request.getParameter("end"))
            ));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException();
        }
        response.sendRedirect(request.getContextPath() + "/rents");
    }
}