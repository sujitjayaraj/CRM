package com.sujit.crm.service;

import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.User;

import java.util.List;

public interface ClientService {

    /**Returns a client object based on the client id
     *
     * @param id client id
     * @return Client object
     */
    Client findById(Long id);

    /**Returns a client object based on the company name
     *
     * @param name client name
     * @return Client object
     */
    Client findByName(String name);

    /**Returns all the clients
     *
     * @return List of all clients
     */
    List<Client> findAll();

    /**Returns String list of possible client statuses
     *
     * @return List of String statuses
     */
    List<String> getStatusList();

    /**Saves a Client object and returns the id of the saved client
     *
     * @param client Client object to be saved
     * @return id of the saved client
     */
    Long saveClient(Client client);

    /**Deletes a client based on the client id
     *
     * @param id client id
     */
    void deleteClient(Long id);

    /**Save Client with currently logged user as the employee managing the client
     *
     * @param client Client object to be saved
     * @return id of the saved client
     */
    Long saveClientWithLoggedUser(Client client);

    /**Returns List of Clients managed by the User as the employee
     *
     * @param user employee managing the clients
     * @return List of clients managed by the employee
     */
    List<Client> findByUser(User user);
}
