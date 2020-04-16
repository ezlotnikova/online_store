package com.gmail.ezlotnikova.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import com.gmail.ezlotnikova.service.PasswordService;
import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import com.gmail.ezlotnikova.service.model.ShowUserDTO;
import com.gmail.ezlotnikova.service.model.UserDTO;
import com.gmail.ezlotnikova.service.util.converter.UserConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.ACTION_FORBIDDEN;
import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.FAILED_TO_EXECUTE;
import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final Validator validator;
    private final PasswordService passwordService;

    public UserServiceImpl(
            UserRepository userRepository, UserConverter userConverter,
            Validator validator, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.validator = validator;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional
    public AddUserDTO add(AddUserDTO userDTO) {
        Set<ConstraintViolation<AddUserDTO>> violations = validator.validate(userDTO);
        if (violations.isEmpty()) {
            User user = userConverter.convertAddUserDTOToDatabaseObject(userDTO);
            User addedUser = userRepository.persist(user);
            return userConverter.convertDatabaseObjectToAddUserDTO(addedUser);
        } else {
            throw new IllegalArgumentException("Can't add user: invalid data provided");
        }
    }

    @Override
    @Transactional
    public Page<ShowUserDTO> findPaginatedAndOrderedByEmail(int pageNumber, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        return userRepository.findPaginatedAndOrderedByEmail(pageRequest)
                .map(userConverter::convertDatabaseObjectToShowUserDTO);
    }

    @Override
    @Transactional
    @Nullable
    public ShowUserDTO findUserById(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            return userConverter.convertDatabaseObjectToShowUserDTO(user);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public UserDTO loadUserByEmail(String email) {
        User user = userRepository.loadUserByEmail(email);
        if (user != null) {
            return userConverter.getUserDTOFromDatabaseObject(user);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public ExecutionResult updateUserRoleById(Long id, UserRoleEnum newRole) {
        User user = userRepository.findById(id);
        if (user != null) {
            if (user.getRole().equals(UserRoleEnum.ADMINISTRATOR) && isTheOnlyAdministrator()) {
                return ExecutionResult.error(ACTION_FORBIDDEN, "Changing role for the only administrator is forbidden. ");
            } else {
                user.setRole(newRole);
                userRepository.merge(user);
                return ExecutionResult.ok();
            }
        } else {
            return ExecutionResult.error(NO_OBJECT_FOUND, "No user with id " + id + " found. ");
        }
    }

    @Override
    @Transactional
    public ExecutionResult deleteById(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            if (user.getRole().equals(UserRoleEnum.ADMINISTRATOR) && isTheOnlyAdministrator()) {
                return ExecutionResult.error(ACTION_FORBIDDEN, "Deleting the only administrator is forbidden. ");
            } else {
                userRepository.remove(user);
                return ExecutionResult.ok();
            }
        } else {
            return ExecutionResult.error(NO_OBJECT_FOUND, "No user with id " + id + " found. ");
        }
    }

    @Override
    public ExecutionResult generatePasswordAndSendEmail(Long userId) {
        try {
            passwordService.generatePasswordAndSendEmail(userId);
            return ExecutionResult.ok();
        } catch (MailException e) {
            logger.error(e.getMessage(), e);
            return ExecutionResult.error(FAILED_TO_EXECUTE, "Email to user can't be sent. Password wasn't changed");
        }
    }

    @Transactional
    private boolean isTheOnlyAdministrator() {
        return userRepository.countAdministrators() == 1L;
    }

}