package com.epf.rentmanager.service;

import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
	private ClientDao clientDao;

	@Autowired
	private ClientService(ClientDao clientDao){
		this.clientDao = clientDao;
	}

	private Client verifClient(Client client) throws ServiceException {
		if (client.nom().isEmpty() || client.prenom().isEmpty())
			throw new ServiceException("Nom ou prénom ne doit pas être vide");
		if (client.naissance() == null || client.naissance().isAfter(java.time.LocalDate.now().minusYears(18)))
			throw new ServiceException("Date de naissance invalide");
		String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		if (!client.email().matches(regex))
			throw new ServiceException("Email invalide");

		return new Client(
				client.id(),
				client.nom().toUpperCase(),
				client.prenom(),
				client.email(),
				client.naissance()
		);
	}

	public long create(Client client) throws ServiceException {
		client = verifClient(client);

		try {
			return clientDao.create(client);
		} catch (DaoException e) {
            throw new ServiceException();
        }
    }

	public Client findById(long id) throws ServiceException {

		try {
			Optional<Client> client = clientDao.findById(id);
			if (client.isEmpty()) {
				throw new ServiceException("Client non trouvé");
			} else {
				return client.get();
			}
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public List<Client> findAll() throws ServiceException {
		try {
			return clientDao.findAll();
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public long delete(Client client) throws ServiceException {
		try {
			return clientDao.delete(client);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public int count() throws ServiceException {
		try {
			return clientDao.count();
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public void update(Client client) throws ServiceException {
		client = verifClient(client);

		try {
			clientDao.update(client);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}
}
