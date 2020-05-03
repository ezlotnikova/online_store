package com.gmail.ezlotnikova.web.controller.web;

import com.gmail.ezlotnikova.service.PasswordService;
import com.gmail.ezlotnikova.service.UserDetailsService;
import com.gmail.ezlotnikova.service.model.UserDTO;
import com.gmail.ezlotnikova.service.model.UserDetailsDTO;
import com.gmail.ezlotnikova.service.security.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_ADDRESS;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_FIRST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_LAST_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_ROLE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.USER_TELEPHONE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ProfileController.class)
public class ProfileControllerShowProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    PasswordService passwordService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_returnView() throws Exception {
        AppUser appUser = getAppUser();
        UserDetailsDTO profile = getUserDetailsDTO();
        Long id = Long.parseLong(VALID_ID);
        when(userDetailsService.getUserDetailsById(id)).thenReturn(profile);
        mockMvc.perform(
                get("/profile")
                        .with(user(appUser))
        ).andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    //    @Test
    //    @WithMockUser(roles = "ADMINISTRATOR")
    //    void whenUnauthorizedUser_returnForbidden() throws Exception {
    //        AppUser appUser = getUnauthorizedUser();
    //        mockMvc.perform(
    //                get("/profile")
    //                        .with(user(appUser))
    //        ).andExpect(status().isForbidden())
    //                .andExpect(view().name("error"))
    //                .andExpect(content().string(containsString(ACCESS_DENIED_MESSAGE)));
    //    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void whenValidRequest_callBusinessLogic() throws Exception {
        AppUser appUser = getAppUser();
        UserDetailsDTO profile = getUserDetailsDTO();
        Long id = Long.parseLong(VALID_ID);
        when(userDetailsService.getUserDetailsById(id)).thenReturn(profile);
        mockMvc.perform(
                get("/profile")
                        .with(user(appUser))
        ).andExpect(status().isOk());
        verify(userDetailsService, times(1)).getUserDetailsById(id);
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnProfile() throws Exception {
        AppUser appUser = getAppUser();
        UserDetailsDTO profile = getUserDetailsDTO();
        Long id = Long.parseLong(VALID_ID);
        when(userDetailsService.getUserDetailsById(id)).thenReturn(profile);
        mockMvc.perform(
                get("/profile")
                        .with(user(appUser))
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(USER_FIRST_NAME)))
                .andExpect(content().string(containsString(USER_LAST_NAME)));
    }

    private AppUser getAppUser() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setRole(USER_ROLE);
        return new AppUser(user);
    }

    private AppUser getUnauthorizedUser() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        return new AppUser(user);
    }

    private UserDetailsDTO getUserDetailsDTO() {
        UserDetailsDTO profile = new UserDetailsDTO();
        profile.setId(Long.parseLong(VALID_ID));
        profile.setFirstName(USER_FIRST_NAME);
        profile.setLastName(USER_LAST_NAME);
        profile.setAddress(USER_ADDRESS);
        profile.setTelephone(USER_TELEPHONE);
        return profile;
    }

}