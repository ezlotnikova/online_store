package com.gmail.ezlotnikova.service.util.converter;

import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import com.gmail.ezlotnikova.service.model.ShowUserDTO;
import com.gmail.ezlotnikova.service.model.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User convertAddUserDTOToDatabaseObject(AddUserDTO userDTO) {
        User user = new User();
        user.setEmail(
                userDTO.getEmail());
        user.setRole(
                userDTO.getRole());
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(
                userDTO.getLastName());
        userDetails.setFirstName(
                userDTO.getFirstName());
        userDetails.setPatronymicName(
                userDTO.getPatronymicName());
        userDetails.setUser(user);
        user.setUserDetails(userDetails);
        return user;
    }

    public AddUserDTO convertDatabaseObjectToAddUserDTO(User user) {
        AddUserDTO userDTO = new AddUserDTO();
        userDTO.setId(
                user.getId());
        userDTO.setEmail(
                user.getEmail());
        userDTO.setRole(
                user.getRole());
        UserDetails userDetails = user.getUserDetails();
        userDTO.setLastName(
                userDetails.getLastName());
        userDTO.setFirstName(
                userDetails.getFirstName());
        userDTO.setPatronymicName(
                userDetails.getPatronymicName());
        return userDTO;
    }

    public ShowUserDTO convertDatabaseObjectToShowUserDTO(User user) {
        ShowUserDTO userDTO = new ShowUserDTO();
        userDTO.setId(
                user.getId());
        UserDetails userDetails = user.getUserDetails();
        userDTO.setLastName(
                userDetails.getLastName());
        userDTO.setFirstName(
                userDetails.getFirstName());
        userDTO.setPatronymicName(
                userDetails.getPatronymicName());
        userDTO.setEmail(
                user.getEmail());
        userDTO.setRole(
                user.getRole());
        return userDTO;
    }

    public UserDTO getUserDTOFromDatabaseObject(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(
                user.getId());
        userDTO.setUsername(
                user.getEmail());
        userDTO.setPassword(
                user.getPassword());
        userDTO.setRole(
                user.getRole());
        userDTO.setEnabled(
                user.getEnabled());
        return userDTO;
    }

}