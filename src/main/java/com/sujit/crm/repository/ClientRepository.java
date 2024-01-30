package com.sujit.crm.repository;

import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByName(String name);
    List<Client> findByNameOrderByNameAsc(String name);
    List<Client> findByNameAndAddressCityOrderByNameAsc(String name, String city);

    List<Client> findByUser(User user);
    List<Client> findByUserOrderByNameAsc(User user);
    List<Client> findByUserIn(List<User> users);
    List<Client> findByCreatedOnAfter(LocalDate date);
    List<Client> findByCreatedOnBetween(LocalDate startDate, LocalDate endDate);
    List<Client> findByStatusOrderByNameAsc(String status);
    List<Client> findByStatusAndAddressCityOrderByNameAsc(String status, String city);
    List<Client> findByAddressCityOrderByNameAsc(String city);
    List<Client> findByAddressStateOrderByNameAsc(String state);
    List<Client> findByTin(String tin);
}
