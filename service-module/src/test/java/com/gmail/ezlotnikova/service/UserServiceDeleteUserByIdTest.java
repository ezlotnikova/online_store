package com.gmail.ezlotnikova.service;

import javax.validation.Validator;

import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import com.gmail.ezlotnikova.service.constant.ErrorCodeConstant;
import com.gmail.ezlotnikova.service.constant.ResultTypeEnum;
import com.gmail.ezlotnikova.service.constant.ServiceTestConstant;
import com.gmail.ezlotnikova.service.impl.UserServiceImpl;
import com.gmail.ezlotnikova.service.util.converter.UserConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceDeleteUserByIdTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private Validator validator;
    @Mock
    private PasswordService passwordService;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(
                userRepository, userConverter, validator, passwordService);
    }

    @Test
    public void deleteExistingUserById_returnExecutedSuccessfully() {
        Long id = 1L;
        User user = getUser();
        user.setRole(UserRoleEnum.CUSTOMER_USER);
        user.setId(id);
        when(userRepository.findById(id))
                .thenReturn(user);
        Assertions.assertThat(userService.deleteById(id).getResultType())
                .isEqualTo(ResultTypeEnum.EXECUTED_SUCCESSFULLY);
    }

    @Test
    public void deleteNonExistingUser_returnNoObjectFound() {
        Long id = 1L;
        User user = getUser();
        user.setId(id);
        when(userRepository.findById(id))
                .thenReturn(null);
        Assertions.assertThat(userService.deleteById(id).getErrorCode())
                .isEqualTo(ErrorCodeConstant.NO_OBJECT_FOUND);
    }

    @Test
    public void deleteLastAdministrator_returnActionForbidden() {
        Long id = 1L;
        User user = getUser();
        user.setId(id);
        when(userRepository.findById(id))
                .thenReturn(user);
        when(userRepository.countAdministrators())
                .thenReturn(1L);
        Assertions.assertThat(userService.deleteById(id).getErrorCode())
                .isEqualTo(ErrorCodeConstant.ACTION_FORBIDDEN);
    }

    private User getUser() {
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