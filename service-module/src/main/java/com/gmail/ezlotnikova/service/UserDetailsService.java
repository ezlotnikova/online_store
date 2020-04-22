package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.UserDetailsDTO;

public interface UserDetailsService {

    UserDetailsDTO getUserDetailsById(Long id);

    ExecutionResult updateUserDetails(Long id, UserDetailsDTO pageProfile);

}