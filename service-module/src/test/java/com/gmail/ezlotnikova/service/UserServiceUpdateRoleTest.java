package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import com.gmail.ezlotnikova.service.constant.ErrorCodeConstant;
import com.gmail.ezlotnikova.service.constant.ResultTypeEnum;
import com.gmail.ezlotnikova.service.constant.ServiceTestConstant;
import com.gmail.ezlotnikova.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUpdateRoleTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordService passwordService;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(
                userRepository, passwordService);
    }

    @Test
    public void updateRoleToExistingUser_returnExecutedSuccessfully() {
        Long id = 1L;
        User user = getUser();
        user.setId(id);
        user.setRole(UserRoleEnum.SALE_USER);
        UserRoleEnum newRole = UserRoleEnum.CUSTOMER_USER;
        user.setId(id);
        when(userRepository.findById(id))
                .thenReturn(user);
        Assertions.assertThat(userService.updateUserRoleById(id, newRole).getResultType()
                == ResultTypeEnum.EXECUTED_SUCCESSFULLY);
    }

    @Test
    public void updateRoleToNonExistingUser_returnNoObjectFound() {
        Long id = 1L;
        User user = getUser();
        user.setId(id);
        user.setRole(UserRoleEnum.ADMINISTRATOR);
        UserRoleEnum newRole = UserRoleEnum.CUSTOMER_USER;
        when(userRepository.findById(id))
                .thenReturn(null);
        Assertions.assertThat(userService.updateUserRoleById(id, newRole).getErrorCode()
                == ErrorCodeConstant.NO_OBJECT_FOUND);
    }

    @Test
    public void updateRoleToLastAdministrator_returnActionForbidden() {
        Long id = 1L;
        User user = getUser();
        user.setId(id);
        user.setRole(UserRoleEnum.ADMINISTRATOR);
        UserRoleEnum newRole = UserRoleEnum.CUSTOMER_USER;
        when(userRepository.findById(id))
                .thenReturn(user);
        when(userRepository.getCountOfUsersByRole(UserRoleEnum.ADMINISTRATOR))
                .thenReturn(1L);
        Assertions.assertThat(userService.updateUserRoleById(id, newRole).getErrorCode()
                == ErrorCodeConstant.ACTION_FORBIDDEN);
    }

    private User getUser() {
        User user = new User();
        user.setEmail(ServiceTestConstant.EMAIL);
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(ServiceTestConstant.LAST_NAME);
        userDetails.setFirstName(ServiceTestConstant.FIRST_NAME);
        userDetails.setPatronymicName(ServiceTestConstant.PATRONYMIC_NAME);
        userDetails.setUser(user);
        user.setUserDetails(userDetails);
        return user;
    }

}