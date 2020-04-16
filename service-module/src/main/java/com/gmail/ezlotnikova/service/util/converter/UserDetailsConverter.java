package com.gmail.ezlotnikova.service.util.converter;

import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.model.UserDetailsDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsConverter {

    public UserDetailsDTO convertDatabaseObjectToUserDetailsDTO(User user) {
        UserDetailsDTO profile = new UserDetailsDTO();
        profile.setId(
                user.getId());
        UserDetails userDetails = user.getUserDetails();
        profile.setFirstName(
                userDetails.getFirstName());
        profile.setLastName(
                userDetails.getLastName());
        if (userDetails.getAddress() == null) {
            profile.setAddress("");
        } else {
            profile.setAddress(
            userDetails.getAddress());
        }
        if (userDetails.getTelephone() == null) {
            profile.setTelephone("");
        } else {
            profile.setTelephone(
                    userDetails.getTelephone());
        }
        return profile;
    }

}