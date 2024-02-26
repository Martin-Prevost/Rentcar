package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.model.Vehicle;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleDao {
	
	private static VehicleDao instance = null;
	private VehicleDao() {}
	public static VehicleDao getInstance() {
		if(instance == null) {
			instance = new VehicleDao();
		}
		return instance;
	}
	
	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String COUNT_VEHICLES_QUERY = "SELECT COUNT(id) FROM Vehicle;";
	
	public long create(Vehicle vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, vehicle.constructeur());
			ps.setString(2, vehicle.modele());
			ps.setInt(3, vehicle.nbPlaces());

			ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getLong(1);
			} else {
				throw new DaoException();
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public long delete(Vehicle vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(DELETE_VEHICLE_QUERY)) {
			ps.setLong(1, vehicle.id());
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public Optional<Vehicle> findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_VEHICLE_QUERY)) {
			ps.setLong(1, id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return Optional.of(new Vehicle(
						resultSet.getLong("id"),
						resultSet.getString("constructeur"),
						resultSet.getString("modele"),
						resultSet.getInt("nb_places")));
			} else {
				throw new DaoException();
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public List<Vehicle> findAll() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_VEHICLES_QUERY)) {
			ResultSet resultSet = ps.executeQuery();
			List<Vehicle> vehicles = new ArrayList<>();
			while (resultSet.next()) {
				vehicles.add(new Vehicle(
						resultSet.getLong("id"),
						resultSet.getString("constructeur"),
						resultSet.getString("modele"),
						resultSet.getInt("nb_places")));
			}
			return vehicles;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public int count() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_VEHICLES_QUERY)) {
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new DaoException();
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}
}
