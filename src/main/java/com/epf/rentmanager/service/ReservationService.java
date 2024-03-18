package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dto.ReservationClientDto;
import com.epf.rentmanager.dto.ReservationDto;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    @Autowired
    private ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public long create(Reservation reservation) throws ServiceException {
        try {
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

    public List<ReservationDto> findResaByClientId(long clientId) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public List<Reservation> findResaByVehicleId(long vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(vehicleId);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public List<ReservationClientDto> findAll() throws ServiceException {
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

    public ReservationClientDto findById(long id) throws ServiceException {
        try {
            return reservationDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public void update(Reservation reservation) throws ServiceException {
        try {
            reservationDao.update(reservation);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }
}
