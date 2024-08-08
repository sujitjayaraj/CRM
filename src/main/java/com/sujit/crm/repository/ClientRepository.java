package com.sujit.crm.repository;

import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByNameIgnoreCase(String name);
    List<Client> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
    List<Client> findByNameContainingIgnoreCaseAndAddressCityOrderByNameAsc(String name, String city);

    List<Client> findByUser(User user);
    List<Client> findByUserOrderByNameAsc(User user);
    List<Client> findByUserIn(List<User> users);

    List<Client> findByCreatedAfter(LocalDateTime created);
    List<Client> findByCreatedBetween(LocalDateTime start, LocalDateTime end);

    List<Client> findByStatusAndAddressCityOrderByNameAsc(String status, String city);
    List<Client> findByStatusOrderByNameAsc(String status);

    List<Client> findByAddressCityOrderByNameAsc(String city);
    List<Client> findByAddressCityContainingIgnoreCaseOrderByNameAsc(String city);
    List<Client> findByAddressStateOrderByNameAsc(String state);

    List<Client> findByTinContaining(String tin);

}
