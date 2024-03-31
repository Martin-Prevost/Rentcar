package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientDaoTest {

    @InjectMocks
    private ClientDao clientDao;

    @Mock(lenient = true)
    private Connection connection;

    @Mock(lenient = true)
    private PreparedStatement preparedStatement;

    @Mock(lenient = true)
    private ResultSet resultSet;

    @Test
    void create_should_throw_DaoException_when_email_already_exists() throws SQLException, DaoException {
        Client client = new Client(1, "nom", "prenom", "unique@gmail.com", java.time.LocalDate.now().minusYears(19));
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        assertThrows(DaoException.class, () -> {
            clientDao.create(client);
        });
    }

    @Test
    void delete_should_throw_DaoException_when_sql_exception_occurs() throws SQLException {
        Client client = new Client(1, "nom", "prenom", "unique@gmail.com", java.time.LocalDate.now().minusYears(19));
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);

        assertThrows(DaoException.class, () -> {
            clientDao.delete(client);
        });
    }

    @Test
    void update_should_throw_DaoException_when_sql_exception_occurs() throws SQLException {
        Client client = new Client(1, "nom", "prenom", "unique@gmail.com", java.time.LocalDate.now().minusYears(19));
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);

        assertThrows(DaoException.class, () -> {
            clientDao.update(client);
        });
    }
}