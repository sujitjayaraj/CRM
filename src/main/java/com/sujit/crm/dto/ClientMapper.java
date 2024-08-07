package com.sujit.crm.dto;

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
    private UserService userService;
    @Autowired
    private ClientService clientService;

    public ClientDto toDto(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(client.getName());
        clientDto.setStatus(client.getStatus());
        clientDto.setTin(client.getTin());
        clientDto.setContactFirstName(client.getContactPerson().getFirstName());
        clientDto.setContactLastName(client.getContactPerson().getLastName());
        clientDto.setContactEmail(client.getContactPerson().getEmail());
        clientDto.setContactPhone(client.getContactPerson().getPhone());
        clientDto.setCity(client.getAddress().getCity());
        clientDto.setState(client.getAddress().getState());
        clientDto.setCountry(client.getAddress().getCountry());
        clientDto.setStreet(client.getAddress().getStreet());
        clientDto.setPincode(client.getAddress().getPincode());
        clientDto.setUserEmail(client.getUser().getEmail());
        return clientDto;
    }

    public Client toEntity(ClientDto clientDto) {
        Client client = new Client();
        client.setName(clientDto.getName());
        client.setStatus(clientDto.getStatus());
        client.setTin(clientDto.getTin());
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setFirstName(clientDto.getContactFirstName());
        contactPerson.setLastName(clientDto.getContactLastName());
        contactPerson.setEmail(clientDto.getContactEmail());
        contactPerson.setPhone(clientDto.getContactPhone());
        client.setContactPerson(contactPerson);
        Address address = new Address();
        address.setCity(clientDto.getCity());
        address.setState(clientDto.getState());
        address.setCountry(clientDto.getCountry());
        address.setStreet(clientDto.getStreet());
        address.setPincode(clientDto.getPincode());
        client.setAddress(address);

        try {
            client.setUser(userService.findByEmail(clientDto.getUserEmail()));
        } catch (NullPointerException e) {
            e.getMessage();
            e.printStackTrace();
        }

        return client;
    }
}
