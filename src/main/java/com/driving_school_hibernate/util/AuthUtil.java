package com.driving_school_hibernate.util;

import com.driving_school_hibernate.bo.BOFactory;
import com.driving_school_hibernate.bo.custom.UsersBO;
import com.driving_school_hibernate.dto.Roles;
import com.driving_school_hibernate.dto.UsersDTO;

import java.sql.SQLException;

public class AuthUtil {

    // Default admin credentials
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin1234";
    private static final Roles DEFAULT_ADMIN_ROLE = Roles.ADMIN;

    private static String currentUser;
    private static Roles currentRole;
    private static String currentUserId;
    private static UsersDTO currentUserDTO;

    public static boolean authenticate(String username, String password) {
        // First check if it's the default admin
        if (DEFAULT_ADMIN_USERNAME.equals(username) && DEFAULT_ADMIN_PASSWORD.equals(password)) {
            currentUser = username;
            currentRole = DEFAULT_ADMIN_ROLE;
            currentUserId = "ADMIN001";
            currentUserDTO = createDefaultAdminDTO();
            return true;
        }

        // Then check against database users
        try {
            UsersDTO user = findUserByUsername(username);

            if (user != null && user.getPassword().equals(password)) {
                currentUser = username;
                currentRole = Roles.fromString(user.getRole());
                currentUserId = user.getUserId();
                currentUserDTO = user;
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    private static UsersDTO createDefaultAdminDTO() {
        UsersDTO admin = new UsersDTO();
        admin.setUserId("ADMIN001");
        admin.setUsername(DEFAULT_ADMIN_USERNAME);
        admin.setEmail("admin@elitedriving.com");
        admin.setPassword(DEFAULT_ADMIN_PASSWORD);
        admin.setRole("Admin");
        return admin;
    }

    private static UsersDTO findUserByUsername(String username) {
        try {
            UsersBO usersBO = BOFactory.getInstance().getBO(com.driving_school_hibernate.bo.BOTypes.USERS);
            return usersBO.findAllUsers().stream()
                    .filter(user -> user.getUsername().equalsIgnoreCase(username))
                    .findFirst()
                    .orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getters
    public static String getCurrentUser() {
        return currentUser;
    }

    public static Roles getCurrentRole() {
        return currentRole;
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }

    public static UsersDTO getCurrentUserDTO() {
        return currentUserDTO;
    }

    public static boolean isAdmin() {
        return Roles.ADMIN.equals(currentRole);
    }

    public static boolean isUser() {
        return Roles.USER.equals(currentRole);
    }

    public static void logout() {
        currentUser = null;
        currentRole = null;
        currentUserId = null;
        currentUserDTO = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // Update current user info (for profile updates)
    public static void updateCurrentUserInfo(String newUsername) {
        if (newUsername != null && !newUsername.isEmpty()) {
            currentUser = newUsername;
            if (currentUserDTO != null) {
                currentUserDTO.setUsername(newUsername);
            }
        }
    }
}