package org.example.keycloakrestapis.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/paginated-ldap-users")
    public ResponseEntity<Page<UserRepresentation>> getLdapUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        // Calculate offset and limit from Pageable
        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // Retrieve the users based on the offset and limit.
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .list(offset, pageSize);

        // If your API provides a total count, retrieve it.
        // This might be a separate method such as keycloak.realm(realm).users().count();
        int totalUsers = keycloak.realm(realm).users().count();

        // Wrap the list in a PageImpl with pagination metadata.
        Page<UserRepresentation> page = new PageImpl<>(users, pageable, totalUsers);

        // Return the paged result.
        return ResponseEntity.ok(page);
    }
}
