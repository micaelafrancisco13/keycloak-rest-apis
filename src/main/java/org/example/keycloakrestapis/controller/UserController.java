package org.example.keycloakrestapis.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    public UserController(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @GetMapping("/test")
    public String testAdminEndpoint() {
        return "This is a test endpoint from the admin endpoint";
    }

    @GetMapping("/ldap-users")
    public List<UserRepresentation> getLdapUsers(
            @RequestParam(defaultValue = "0") int first,
            @RequestParam(defaultValue = "20") int max) {
        return keycloak.realm(realm)
                .users()
                .list(first, max);
    }
}
