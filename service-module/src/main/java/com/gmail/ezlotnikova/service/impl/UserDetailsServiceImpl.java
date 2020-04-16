package com.gmail.ezlotnikova.service.impl;

import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.UserDetailsService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.UserDetailsDTO;
import com.gmail.ezlotnikova.service.util.converter.UserDetailsConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserDetailsConverter userDetailsConverter;

    public UserDetailsServiceImpl(UserRepository userRepository, UserDetailsConverter userDetailsConverter) {
        this.userRepository = userRepository;
        this.userDetailsConverter = userDetailsConverter;
    }

    @Override
    @Transactional
    @Nullable
    public UserDetailsDTO getUserDetailsById(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            return userDetailsConverter.convertDatabaseObjectToUserDetailsDTO(user);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public ExecutionResult updateUserDetails(Long id, UserDetailsDTO newDetails) {
        User user = userRepository.findById(id);
        if (user != null) {
            UserDetails userDetails = user.getUserDetails();
            userDetails.setFirstName(newDetails.getFirstName());
            userDetails.setLastName(newDetails.getLastName());
            userDetails.setAddress(newDetails.getAddress());
            userDetails.setTelephone(newDetails.getTelephone());
            user.setUserDetails(userDetails);
            userRepository.merge(user);
            return ExecutionResult.ok();
        } else {
            return ExecutionResult.error(NO_OBJECT_FOUND, "No user with id " + id + " found.");
        }
    }

}