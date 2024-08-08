package com.sujit.crm.service;

import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.User;

import java.util.List;

public interface ClientService {

    /**
     * Returns {@link com.sujit.crm.entity.Client} by given id.
     *
     * @param id of type {@link Long}.
     * @return {@link com.sujit.crm.entity.Client}.
     */
    Client findById(Long id);

    /**
     * Returns {@link com.sujit.crm.entity.Client} by given company name.
     *
     * @param name name of the client company.
     * @return {@link com.sujit.crm.entity.Client}.
     */
    Client findByName(String name);

    /**
     * Returns all {@link com.sujit.crm.entity.Client} entities.
     *
     * @return {@link java.util.List} of {@link com.sujit.crm.entity.Client}.
     */
    List<Client> findAll();

    /**
     * Returns {@link String} {@link java.util.List} of possible client statuses.
     *
     * @return {@link java.util.List} of {@link String} statuses.
     */
    List<String> getStatusList();

    /**
     * Saves the given {@link com.sujit.crm.entity.Client}.
     *
     * @param client The {@link com.sujit.crm.entity.Client} to be saved.
     * @return id of the saved {@link com.sujit.crm.entity.Client}.
     */
    Long saveClient(Client client);

    /**
     * Delete {@link com.sujit.crm.entity.Client} by given id.
     *
     * @param id of the {@link com.sujit.crm.entity.Client} to be deleted.
     */
    void deleteClient(Long id);

    /**
     * Save {@link com.sujit.crm.entity.Client} with currently logged {@link com.sujit.crm.entity.User} as employee managing client.
     *
     * @param client The {@link com.sujit.crm.entity.Client} to be saved.
     * @return id of the saved {@link com.sujit.crm.entity.Client}.
     */
    Long saveClientWithLoggedUser(Client client);

    /**
     * Returns {@link java.util.List} of {@link com.sujit.crm.entity.Client} managed by currently logged {@link com.sujit.crm.entity.User}.
     *
     * @param user The currently logged {@link com.sujit.crm.entity.User}.
     * @return {@link java.util.List} of {@link com.sujit.crm.entity.Client}.
     */
    List<Client> findByUser(User user);
}
