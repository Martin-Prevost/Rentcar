package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ClientService clientService = ClientService.getInstance();
		VehicleService vehicleService = VehicleService.getInstance();
		ReservationService reservationService = ReservationService.getInstance();
		try {
			request.setAttribute("nbClients", clientService.count());
			request.setAttribute("nbVehicles", vehicleService.count());
			request.setAttribute("nbReservations", reservationService.count());
		} catch (ServiceException e) {
			throw new ServletException();
		}
		this.getServletContext().getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
	}
}
