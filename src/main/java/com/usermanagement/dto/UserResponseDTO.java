
package com.usermanagement.dto;

import com.usermanagement.entity.enums.Role;

import java.util.UUID;

public class UserResponseDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean active;

    public UserResponseDTO() {
    }

    public UserResponseDTO(UUID id, String firstName, String lastName, String email, Role role, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
