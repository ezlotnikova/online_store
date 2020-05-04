package com.gmail.ezlotnikova.service;

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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceAddUserTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordService passwordService;
    @Mock
    private UserConverter userConverter;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(
                userRepository, passwordService);
    }

    @Test
    public void addValidUser_returnUserDTO() {
        AddUserDTO newUserDTO = getValidAddUserDTO();
        AddUserDTO expectedReturnedUserDTO = getValidAddUserDTO();
        expectedReturnedUserDTO.setId(1L);
        User newUser = getValidUser();
        User expectedReturnedUser = getValidUser();
        expectedReturnedUser.setId(1L);
        when(userRepository.persist(newUser))
                .thenReturn(expectedReturnedUser);
        AddUserDTO returnedUser = userService.add(newUserDTO);
        verify(userRepository, times(1)).persist(newUser);
        Assertions.assertThat(returnedUser).isNotNull();
        Assertions.assertThat(returnedUser.getId()).isNotNull();
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