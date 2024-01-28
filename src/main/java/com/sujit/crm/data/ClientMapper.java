package com.sujit.crm.data;

import com.sujit.crm.entity.Address;
import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.ContactPerson;
import com.sujit.crm.service.ClientService;
import com.sujit.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    @Autowired
    private UserService userServices;
    @Autowired
    private ClientService clientService;

    public ClientData mapToClientData(Client client){
        ClientData clientData = new ClientData();
        clientData.setName(client.getName());
        clientData.setStatus(client.getStatus());
        clientData.setTin(client.getTin());
        clientData.setUserEmail(client.getUser().getEmail());
        clientData.setContactFirstname(client.getContactPerson().getFirstname());
        clientData.setContactLastname(client.getContactPerson().getLastname());
        clientData.setContactEmail(client.getContactPerson().getEmail());
        clientData.setContactPhone(client.getContactPerson().getPhone());
        clientData.setStreet(client.getAddress().getStreet());
        clientData.setCity(client.getAddress().getCity());
        clientData.setState(client.getAddress().getState());
        clientData.setPincode(client.getAddress().getPincode());
        clientData.setCountry(client.getAddress().getCountry());
        return clientData;
    }

    public Client mapToClient(ClientData clientData){
        Client client = new Client();
        client.setName(clientData.getName());
        client.setStatus(clientData.getStatus());
        client.setTin(clientData.getTin());
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setFirstname(clientData.getContactFirstname());
        contactPerson.setLastname(clientData.getContactLastname());
        contactPerson.setEmail(clientData.getContactEmail());
        contactPerson.setPhone(clientData.getContactPhone());
        client.setContactPerson(contactPerson);
        Address address = new Address();
        address.setStreet(clientData.getStreet());
        address.setCity(clientData.getCity());
        address.setState(clientData.getState());
        address.setPincode(clientData.getPincode());
        address.setCountry(clientData.getCountry());
        client.setAddress(address);

        try{
            client.setUser(userServices.findByEmail(clientData.getUserEmail()));
        }
        catch (NullPointerException e){
            System.out.println("User not found for email: "+clientData.getUserEmail());
        }
        return client;
    }
}
