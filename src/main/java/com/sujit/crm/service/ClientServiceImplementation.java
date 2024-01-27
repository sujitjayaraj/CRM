package com.sujit.crm.service;

import com.sujit.crm.AuthenticationFacade;
import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.AddressRepository;
import com.sujit.crm.repository.ClientRepository;
import com.sujit.crm.repository.ContactPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class ClientServiceImplementation implements ClientService{

    @Autowired
    private AuthenticationFacade authenticationFacade;
    private final ClientRepository clientRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final AddressRepository addressRepository;
    public ClientServiceImplementation(ClientRepository clientRepository, ContactPersonRepository contactPersonRepository, AddressRepository addressRepository) {
        this.clientRepository = clientRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.addressRepository = addressRepository;
    }
    @Override
    public Client findById(Long id) throws NoSuchElementException {
        return clientRepository.findById(id).get();
    }
    @Override
    public Client findByName(String name) {
        return clientRepository.findByName(name);
    }
    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
    @Override
    public List<String> getStatusList() {
        return Arrays.asList("lead", "client", "lost");
    }
    @Override
    public Long saveClientWithLoggedUser(Client client) {
        User user = authenticationFacade.getAuthenticatedUser();
        client.setUser(user);
        client.setCreatedOn(LocalDateTime.now());
        clientRepository.save(client);
        return client.getId();
    }
    @Override
    public Long saveClient(Client client) {
        client.setCreatedOn(LocalDateTime.now());
        clientRepository.save(client);
        return client.getId();
    }
    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
    @Override
    public List<Client> findByUser(User user) {
        return clientRepository.findByUser(user);
    }
}
