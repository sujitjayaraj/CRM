package com.sujit.crm.api;

import com.sujit.crm.entity.Client;
import com.sujit.crm.service.ClientService;
import com.sujit.crm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientResource {
    private final ClientService clientService;
    private final UserService userService;
    public ClientResource(ClientService clientService, UserService userService) {
        this.clientService = clientService;
        this.userService = userService;
    }
    @GetMapping("/{id}")
    ResponseEntity<Client> getClient(@PathVariable Long id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }
    @GetMapping
    ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.findAll());
    }
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.accepted().build();
    }
    @PostMapping("/{userEmail}")
    ResponseEntity<Long> createClient(@PathVariable String userEmail, @Valid @RequestBody Client client) {
        client.setUser(userService.findByEmail(userEmail));
        Long id = clientService.saveClient(client);
        return ResponseEntity.ok(id);
    }
    @PutMapping("/{userEmail}")
    ResponseEntity<Void> updateUser(@PathVariable String userEmail, @Valid @RequestBody Client client) {
        client.setUser(userService.findByEmail(userEmail));
        clientService.saveClient(client);
        return ResponseEntity.accepted().build();
    }
}