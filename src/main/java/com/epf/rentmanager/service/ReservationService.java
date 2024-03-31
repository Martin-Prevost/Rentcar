package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dto.ReservationClientDto;
import com.epf.rentmanager.dto.ReservationClientVehicleDto;
import com.epf.rentmanager.dto.ReservationVehicleDto;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    @Autowired
    private ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    private void checkDoubleBooking(Reservation reservation) throws ServiceException {
        try {
            List<ReservationClientDto> reservations = reservationDao.findResaByVehicleId(reservation.vehicleId());
            for (ReservationClientDto existingReservation : reservations) {
                if ((reservation.debut().isAfter(existingReservation.debut()) && reservation.debut().isBefore(existingReservation.fin()))
                || (reservation.fin().isAfter(existingReservation.debut()) && reservation.fin().isBefore(existingReservation.fin()))
                || (reservation.debut().isEqual(existingReservation.debut()) && reservation.fin().isEqual(existingReservation.fin()))) {
                    throw new ServiceException("The vehicle is already reserved on this date.");
                }
            }
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    private void checkMaxBooking(Reservation reservation) throws ServiceException {
        if (reservation.fin().minusDays(7).isAfter(reservation.debut())) {
            throw new ServiceException("The maximum booking duration is 7 days.");
        }
        if (reservation.fin().minusDays(30).isAfter(reservation.debut())) {
            throw new ServiceException("A vehicle cannot be booked for 30 consecutive days without a break.");
        }
    }


    public long create(Reservation reservation) throws ServiceException {
        try {
            checkDoubleBooking(reservation);
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public long delete(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.delete(reservation);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public List<ReservationVehicleDto> findResaByClientId(long clientId) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public List<ReservationClientDto> findResaByVehicleId(long vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(vehicleId);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public List<ReservationClientVehicleDto> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public int count() throws ServiceException {
        try {
            return reservationDao.count();
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public int countByClientId(long clientId) throws ServiceException {
        try {
            return reservationDao.countByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public int countVehiclesByClientId(long clientId) throws ServiceException {
        try {
            return reservationDao.countVehiclesByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public ReservationClientVehicleDto findById(long id) throws ServiceException {
        try {
            return reservationDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public void update(Reservation reservation) throws ServiceException {
        try {
            checkDoubleBooking(reservation);
            reservationDao.update(reservation);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }
}
