package com.gmail.ezlotnikova.service;

import com.gmail.ezlotnikova.repository.UserRepository;
import com.gmail.ezlotnikova.repository.model.User;
import com.gmail.ezlotnikova.repository.model.UserDetails;
import com.gmail.ezlotnikova.service.constant.ServiceTestConstant;
import com.gmail.ezlotnikova.service.impl.UserServiceImpl;
import com.gmail.ezlotnikova.service.model.ShowUserDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.ezlotnikova.service.util.converter.UserConverter.convertToShowUserDTO;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceFindUserByIdTest {

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

    @Disabled
    @Test
    public void findExistingUserById_returnShowUserDTO() {
        Long id = 1L;
        User expectedReturnedUser = getUser();
        expectedReturnedUser.setId(id);
        ShowUserDTO expectedReturnedUserDTO = getShowUserDTO();
        expectedReturnedUserDTO.setId(id);
        when(userRepository.findById(id))
                .thenReturn(expectedReturnedUser);
        when(convertToShowUserDTO(expectedReturnedUser))
                .thenReturn(expectedReturnedUserDTO);
        ShowUserDTO returnedUserDTO = userService.findUserById(id);
        Assertions.assertThat(returnedUserDTO).isNotNull();
        Assertions.assertThat(returnedUserDTO.getId().equals(id));
    }

    @Test
    public void findNotExistingUserById_returnNullPointerException() {
        Long id = 1L;
        when(userRepository.findById(id))
                .thenReturn(null);
        ShowUserDTO returnedUserDTO = userService.findUserById(id);
        Assertions.assertThat(returnedUserDTO).isNull();
    }

    private ShowUserDTO getShowUserDTO() {
        ShowUserDTO user = new ShowUserDTO();
        user.setLastName(ServiceTestConstant.LAST_NAME);
        user.setFirstName(ServiceTestConstant.FIRST_NAME);
        user.setPatronymicName(ServiceTestConstant.PATRONYMIC_NAME);
        user.setEmail(ServiceTestConstant.EMAIL);
        user.setRole(ServiceTestConstant.ROLE);
        return user;
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