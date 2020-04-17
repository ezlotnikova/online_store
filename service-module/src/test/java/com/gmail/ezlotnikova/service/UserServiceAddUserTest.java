package com.gmail.ezlotnikova.service;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.constant.ServiceTestConstant;
import com.gmail.ezlotnikova.service.impl.UserServiceImpl;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import com.gmail.ezlotnikova.service.util.converter.UserConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceAddUserTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private PasswordService passwordService;

    private UserService userService;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        userService = new UserServiceImpl(
                        userRepository, userConverter, validator, passwordService);
    }

    @Test
    public void addValidUser_ValidationPassed() {
        AddUserDTO user = getValidAddUserDTO();
        Set<ConstraintViolation<AddUserDTO>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void addValidUser_returnUserDTO() {
        AddUserDTO newUserDTO = getValidAddUserDTO();
        AddUserDTO expectedReturnedUserDTO = getValidAddUserDTO();
        expectedReturnedUserDTO.setId(1L);
        User newUser = getValidUser();
        User expectedReturnedUser = getValidUser();
        expectedReturnedUser.setId(1L);
        when(userConverter.convertAddUserDTOToDatabaseObject(newUserDTO))
                .thenReturn(newUser);
        when(userRepository.persist(newUser))
                .thenReturn(expectedReturnedUser);
        when(userConverter.convertDatabaseObjectToAddUserDTO(expectedReturnedUser))
                .thenReturn(expectedReturnedUserDTO);
        AddUserDTO returnedUser = userService.add(newUserDTO);
        verify(userRepository, times(1)).persist(newUser);
        Assertions.assertThat(returnedUser).isNotNull();
        Assertions.assertThat(returnedUser.getId()).isNotNull();
    }

    @Test
    public void addInvalidUser_ValidationFailed() {
        AddUserDTO user = getValidAddUserDTO();
        user.setLastName(null);
        Set<ConstraintViolation<AddUserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void addInvalidUser_returnIllegalArgumentException() {
        AddUserDTO newUserDTO = getValidAddUserDTO();
        newUserDTO.setLastName(null);
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.add(newUserDTO),
                "Can't add user: invalid data provided"
        );
    }

    private AddUserDTO getValidAddUserDTO() {
        AddUserDTO user = new AddUserDTO();
        user.setLastName(ServiceTestConstant.LAST_NAME);
        user.setFirstName(ServiceTestConstant.FIRST_NAME);
        user.setPatronymicName(ServiceTestConstant.PATRONYMIC_NAME);
        user.setEmail(ServiceTestConstant.EMAIL);
        user.setRole(ServiceTestConstant.ROLE);
        return user;
    }

    private User getValidUser() {
        User user = new User();
        user.setEmail(ServiceTestConstant.EMAIL);
        user.setRole(ServiceTestConstant.ROLE);
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(ServiceTestConstant.LAST_NAME);
        userDetails.setFirstName(ServiceTestConstant.FIRST_NAME);
        userDetails.setPatronymicName(ServiceTestConstant.PATRONYMIC_NAME);
        userDetails.setUser(user);
        user.setUserDetails(userDetails);
        return user;
    }

}