package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.model.Client;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {
	private static final String CREATE_CLIENT_QUERY ="INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?)";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String COUNT_CLIENTS_QUERY = "SELECT COUNT(id) FROM Client;";
	private static final String UPDATE_CLIENT_QUERY = "UPDATE Client SET nom=?, prenom=?, email=?, naissance=? WHERE id=?;";
	
	public long create(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, client.nom());
			ps.setString(2, client.prenom());
			ps.setString(3, client.email());
			ps.setDate(4, Date.valueOf(client.naissance()));

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
	
	public long delete(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
		     PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY)) {
			ps.setLong(1, client.id());
			return ps.executeUpdate();
		} catch (SQLException e) {
            throw new DaoException();
        }
	}

	public Optional<Client> findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_QUERY)) {
			ps.setLong(1, id);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return Optional.of(new Client(id,
						resultSet.getString("nom"),
						resultSet.getString("prenom"),
						resultSet.getString("email"),
						resultSet.getDate("naissance").toLocalDate()));
			} else {
				throw new DaoException();
			}
		} catch (SQLException e) {
            throw new DaoException();
        }
    }

	public List<Client> findAll() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_CLIENTS_QUERY)) {
			ResultSet resultSet = ps.executeQuery();
			List<Client> clients = new ArrayList<>();
			while (resultSet.next()) {
				clients.add(new Client(resultSet.getLong("id"),
						resultSet.getString("nom"),
						resultSet.getString("prenom"),
						resultSet.getString("email"),
						resultSet.getDate("naissance").toLocalDate()));
			}
			return clients;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public int count() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_CLIENTS_QUERY)) {
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

	public void update(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(UPDATE_CLIENT_QUERY)) {
			ps.setString(1, client.nom());
			ps.setString(2, client.prenom());
			ps.setString(3, client.email());
			ps.setDate(4, Date.valueOf(client.naissance()));
			ps.setLong(5, client.id());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException();
		}
	}
}
