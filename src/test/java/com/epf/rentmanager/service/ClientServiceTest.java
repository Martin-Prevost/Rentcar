package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientDao clientDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create_should_throw_ServiceException_when_nom_is_empty() {
        assertThrows(ServiceException.class, () -> {
            clientService.create(new Client("", "prenom", "email@mail.com",
                    LocalDate.now().minusYears(19)));
        });
    }

    @Test
    void create_should_throw_ServiceException_when_prenom_is_empty() {
        assertThrows(ServiceException.class, () -> {
            clientService.create(new Client("nom", "", "email@mail.com",
                    LocalDate.now().minusYears(19)));
        });
    }

    @Test
    void create_should_throw_ServiceException_when_email_is_invalid() {
        assertThrows(ServiceException.class, () -> {
            clientService.create(new Client("nom", "prenom", "email",
                    LocalDate.now().minusYears(19)));
        });
    }

    @Test
    void create_should_throw_ServiceException_when_naissance_is_null() {
        assertThrows(ServiceException.class, () -> {
            clientService.create(new Client("nom", "prenom", "email@mail.com", null));
        });
    }

    @Test
    void create_should_throw_ServiceException_when_client_is_minor() {
        assertThrows(ServiceException.class, () -> {
            clientService.create(new Client("nom", "prenom", "email@mail.com",
                    LocalDate.now().minusYears(17)));
        });
    }

    @Test
    void create_should_throw_ServiceException_when_dao_throws_DaoException() throws DaoException {
        when(this.clientDao.create(any(Client.class))).thenThrow(DaoException.class);

        assertThrows(ServiceException.class, () -> {
            clientService.create(new Client("nom", "prenom", "email@mail.com",
                    LocalDate.now().minusYears(19)));
        });
    }

    @Test
    void create_should_return_id_when_client_is_created() throws ServiceException, DaoException {
        when(this.clientDao.create(any(Client.class))).thenReturn(1L);
        long id = clientService.create(new Client("nom", "prenom", "email@mail.com",
                LocalDate.now().minusYears(19)));
        assertEquals(id, 1L);
    }
}
