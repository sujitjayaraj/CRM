package com.sujit.crm.api;

import com.sujit.crm.entity.Client;
import com.sujit.crm.service.ClientService;
import com.sujit.crm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    ResponseEntity<Optional<Client>> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteUser(@PathVariable Long id) {
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
    ResponseEntity<String> updateUser(@PathVariable String userEmail, @Valid @RequestBody Client client) {
        client.setUser(userService.findByEmail(userEmail));
        clientService.saveClient(client);
        return ResponseEntity.accepted().build();
    }
}
