package com.sujit.crm.service;

import com.sujit.crm.entity.Address;
import com.sujit.crm.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * Returns the {@link java.util.List} of cities from clients and company offices addresses.
     *
     * @return {@link java.util.List} of cities of type {@link String}.
     */
    public List<String> getCitiesList() {
        List<Address> addresses = addressRepository.findAll();
        Set<String> cities = new HashSet<>();

        for(Address address : addresses) {
            cities.add(address.getCity());
        }

        return List.copyOf(cities);
    }
}
