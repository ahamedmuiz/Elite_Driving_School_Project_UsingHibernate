package com.driving_school_hibernate.bo.custom;

import com.driving_school_hibernate.bo.SuperBO;
import com.driving_school_hibernate.dto.UsersDTO;

import java.sql.SQLException;
import java.util.List;

public interface UsersBO extends SuperBO {
    boolean saveUser(UsersDTO dto) throws SQLException;
    boolean updateUser(UsersDTO dto) throws SQLException;
    boolean deleteUser(String id) throws SQLException;
    UsersDTO findUserById(String id) throws SQLException;
    List<UsersDTO> findAllUsers() throws SQLException;
    String generateNewUserId() throws SQLException;
    List<UsersDTO> searchUsersByName(String name) throws SQLException;
}
