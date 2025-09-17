package com.driving_school_hibernate.bo.custom.impl;

import com.driving_school_hibernate.bo.custom.UsersBO;
import com.driving_school_hibernate.bo.util.EntityDTOConvertor;
import com.driving_school_hibernate.dao.DAOFactory;
import com.driving_school_hibernate.dao.custom.UsersDAO;
import com.driving_school_hibernate.dto.UsersDTO;
import com.driving_school_hibernate.entity.UsersEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UsersBOImpl implements UsersBO {

    private final UsersDAO usersDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.USERS);

    @Override
    public boolean saveUser(UsersDTO dto) throws SQLException {
        return usersDAO.save(EntityDTOConvertor.toEntity(dto));
    }

    @Override
    public boolean updateUser(UsersDTO dto) throws SQLException {
        return usersDAO.update(EntityDTOConvertor.toEntity(dto));
    }

    @Override
    public boolean deleteUser(String id) throws SQLException {
        return usersDAO.delete(id);
    }

    @Override
    public UsersDTO findUserById(String id) throws SQLException {
        return usersDAO.findById(id).map(EntityDTOConvertor::fromEntity).orElse(null);
    }

    @Override
    public List<UsersDTO> findAllUsers() throws SQLException {
        return usersDAO.findAll().stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public String generateNewUserId() throws SQLException {
        String lastId = usersDAO.findLastId().orElse(null);
        if (lastId != null) {
            int num = Integer.parseInt(lastId.replace("U", ""));
            return String.format("U%03d", num + 1);
        }
        return "U001";
    }

    @Override
    public List<UsersDTO> searchUsersByName(String name) throws SQLException {
        return usersDAO.searchByName(name).stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }
}
