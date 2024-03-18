package com.epf.rentmanager.service;

import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.VehicleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
	private VehicleDao vehicleDao;

	@Autowired
	private VehicleService(VehicleDao vehicleDao) {
		this.vehicleDao = vehicleDao;
	}
	
	public long create(Vehicle vehicle) throws ServiceException {
		if (vehicle.constructeur().isEmpty())
			throw new ServiceException("Constructeur ne doit pas être vide");
		if (vehicle.nbPlaces() < 1)
			throw new ServiceException("Nombre de places doit être supérieur à 1");
		try {
			return vehicleDao.create(vehicle);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public Vehicle findById(long id) throws ServiceException {
		try {
			Optional<Vehicle> vehicle = vehicleDao.findById(id);
			if (vehicle.isEmpty()) {
				throw new ServiceException("Véhicule non trouvé");
			} else {
				return vehicle.get();
			}
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public List<Vehicle> findAll() throws ServiceException {
		try {
			return vehicleDao.findAll();
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public long delete(Vehicle vehicle) throws ServiceException {
		try {
			return vehicleDao.delete(vehicle);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public int count() throws ServiceException {
		try {
			return vehicleDao.count();
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public void update(Vehicle vehicle) throws ServiceException {
		try {
			vehicleDao.update(vehicle);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}
}
