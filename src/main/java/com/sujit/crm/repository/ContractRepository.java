package com.sujit.crm.repository;

import com.sujit.crm.entity.Client;
import com.sujit.crm.entity.Contract;
import com.sujit.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByClient(Client client);
    List<Contract> findByAcceptedBy(User user);
}