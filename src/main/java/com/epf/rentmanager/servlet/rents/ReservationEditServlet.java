package com.epf.rentmanager.servlet.rents;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rents/edit")
public class ReservationEditServlet extends HttpServlet {
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
            request.setAttribute("reservation", reservationService.findById(Long.parseLong(request.getParameter("id"))));
            request.setAttribute("clients", clientService.findAll());
            request.setAttribute("vehicles", vehicleService.findAll());
        } catch (ServiceException e) {
            throw new ServletException();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            java.time.LocalDate begin = java.time.LocalDate.parse(request.getParameter("begin"));
            java.time.LocalDate end = java.time.LocalDate.parse(request.getParameter("end"));
            if (begin.isAfter(end)) {
                throw new ServletException("The start date must be before the end date.");
            }
            if (end.minusDays(7).isAfter(begin)) {
                throw new ServletException("The maximum booking duration is 7 days.");
            }

            reservationService.update(new Reservation(
                    Long.parseLong(request.getParameter("id")),
                    Long.parseLong(request.getParameter("client")),
                    Long.parseLong(request.getParameter("car")),
                    begin,
                    end
            ));
        } catch (ServiceException e) {
            throw new ServletException();
        }
        response.sendRedirect(request.getContextPath() + "/rents");
    }
}
