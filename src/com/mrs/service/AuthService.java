package com.mrs.service;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private Map<String, String> users = new HashMap<>();
    private Map<String, String> roles = new HashMap<>();

    public AuthService() {

        users.put("admin", "admin123");
        users.put("user", "user123");

        roles.put("admin", "ADMIN");
        roles.put("user", "USER");
    }

    public String login(String username, String password) {
        if (!users.containsKey(username) || !users.get(username).equals(password)) {
            return null;
        }
        return roles.get(username);
    }
}