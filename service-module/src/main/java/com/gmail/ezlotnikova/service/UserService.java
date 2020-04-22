package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import com.gmail.ezlotnikova.service.model.ShowUserDTO;
import com.gmail.ezlotnikova.service.model.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {

    AddUserDTO add(AddUserDTO user);

    Page<ShowUserDTO> findPaginatedAndOrderedByEmail(int pageNumber);

    ShowUserDTO findUserById(Long id);

    UserDTO loadUserByEmail(String email);

    ExecutionResult generatePasswordAndSendEmail(Long userId);

    ExecutionResult updateUserRoleById(Long id, UserRoleEnum newRoleEnum);

    ExecutionResult deleteById(Long id);

}