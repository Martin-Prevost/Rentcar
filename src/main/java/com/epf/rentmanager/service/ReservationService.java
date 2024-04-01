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

import java.util.ArrayList;
import java.util.Comparator;
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
                || reservation.debut().isEqual(existingReservation.debut()) || reservation.debut().isEqual(existingReservation.fin())
                || reservation.fin().isEqual(existingReservation.debut()) ||reservation.fin().isEqual(existingReservation.fin())) {
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
    }

    private void check30Days(Reservation reservation) throws ServiceException, DaoException {
        List<Reservation> reservations = reservationDao.findResaByVehicleIdReservation(reservation.vehicleId());
        reservations.add(reservation);
        reservations.sort(Comparator.comparing(Reservation::debut));

        int nbDays = 0;
        for (int i = 0; i < reservations.size() - 1; i++) {
            Reservation current = reservations.get(i);
            Reservation next = reservations.get(i + 1);
            if (current.fin().plusDays(1).isEqual(next.fin())) {
                nbDays += current.debut().until(next.fin()).getDays();
                if (nbDays >= 30) {
                    throw new ServiceException("A vehicle cannot be booked for 30 consecutive days without a break.");
                }
            } else {
                nbDays = 0;
            }
        }
    }


    public long create(Reservation reservation) throws ServiceException {
        try {
            checkDoubleBooking(reservation);
            checkMaxBooking(reservation);
            check30Days(reservation);
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

    public List<Reservation> findResaByVehicleIdReservation(long vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleIdReservation(vehicleId);
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
            checkMaxBooking(reservation);
            check30Days(reservation);
            reservationDao.update(reservation);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }
}
