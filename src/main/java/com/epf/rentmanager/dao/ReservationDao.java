package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dto.ReservationClientDto;
import com.epf.rentmanager.dto.ReservationDto;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {
	private static final String CREATE_RESERVATION_QUERY =
		"""
		INSERT INTO Reservation(client_id, vehicle_id, debut, fin) 
		VALUES(?, ?, ?, ?);
		""";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY =
 		"""
 		SELECT Reservation.id, Vehicle.id AS vehicle_id, Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places, debut, fin 
 		FROM Reservation
 		INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id
 		WHERE client_id=?;
 		""";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY =
  		"""
		SELECT id, client_id, debut, fin 
		FROM Reservation 
		WHERE vehicle_id=?;
		""";
	private static final String FIND_RESERVATIONS_QUERY =
  		"""
		SELECT Reservation.id, Vehicle.id AS vehicle_id, Vehicle.constructeur, Vehicle.modele, Vehicle.nb_places, Client.id as client_id, Client.nom, Client.prenom, Client.email, Client.naissance, debut, fin
		FROM Reservation
		INNER JOIN Vehicle ON Reservation.vehicle_id = Vehicle.id
		INNER JOIN Client ON Reservation.client_id = Client.id;
		""";
	private static final String COUNT_RESERVATIONS_QUERY = "SELECT COUNT(id) FROM Reservation;";
	private static final String COUNT_RESERVATIONS_BY_CLIENT_QUERY = "SELECT COUNT(id) FROM Reservation WHERE client_id=?;";
	private static final String COUNT_VEHICLES_BY_CLIENT_QUERY = "SELECT COUNT(DISTINCT(vehicle_id)) FROM Reservation WHERE client_id=?;";

		
	public long create(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY, Statement.RETURN_GENERATED_KEYS)){
			ps.setLong(1, reservation.clientId());
			ps.setLong(2, reservation.vehicleId());
			ps.setDate(3, Date.valueOf(reservation.debut()));
			ps.setDate(4, Date.valueOf(reservation.fin()));

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
	
	public long delete(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
		     PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION_QUERY)) {
			ps.setLong(1, reservation.id());
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	
	public List<ReservationDto> findResaByClientId(long clientId) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY)) {
			ps.setLong(1, clientId);
			ResultSet resultSet = ps.executeQuery();
			List<ReservationDto> reservations = new ArrayList<>();
			while (resultSet.next()) {
				reservations.add(new ReservationDto(
						resultSet.getLong("id"),
						new Vehicle(
								resultSet.getLong("vehicle_id"),
								resultSet.getString("constructeur"),
								resultSet.getString("modele"),
								resultSet.getInt("nb_places")
						),
						resultSet.getDate("debut").toLocalDate(),
						resultSet.getDate("fin").toLocalDate())
				);
			}
			return reservations;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}
	
	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY)) {
			ps.setLong(1, vehicleId);
			ResultSet resultSet = ps.executeQuery();
			List<Reservation> reservations = new ArrayList<>();
			while (resultSet.next()) {
				reservations.add(new Reservation(
						resultSet.getLong("id"),
						resultSet.getLong("client_id"),
						resultSet.getLong("vehicle_id"),
						resultSet.getDate("debut").toLocalDate(),
						resultSet.getDate("fin").toLocalDate())
				);
			}
			return reservations;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public List<ReservationClientDto> findAll() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_QUERY)) {
			ResultSet resultSet = ps.executeQuery();
			List<ReservationClientDto> reservations = new ArrayList<>();
			while (resultSet.next()) {
				reservations.add(new ReservationClientDto(
						resultSet.getLong("id"),
						new Vehicle(
								resultSet.getLong("vehicle_id"),
								resultSet.getString("constructeur"),
								resultSet.getString("modele"),
								resultSet.getInt("nb_places")
						),
						new Client(
								resultSet.getLong("client_id"),
								resultSet.getString("nom"),
								resultSet.getString("prenom"),
								resultSet.getString("email"),
								resultSet.getDate("naissance").toLocalDate()
						),
						resultSet.getDate("debut").toLocalDate(),
						resultSet.getDate("fin").toLocalDate()));
			}
			return reservations;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public int count() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_RESERVATIONS_QUERY)) {
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

	public int countByClientId(long clientId) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_RESERVATIONS_BY_CLIENT_QUERY)) {
			ps.setLong(1, clientId);
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

	public int countVehiclesByClientId(long clientId) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_VEHICLES_BY_CLIENT_QUERY)) {
			ps.setLong(1, clientId);
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
