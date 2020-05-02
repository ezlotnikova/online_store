package com.gmail.ezlotnikova.web.controller.web;

import java.math.BigDecimal;

import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.web.controller.web.ItemController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_DESCRIPTION;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_UNIQUE_NUMBER;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerShowByUuidTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ItemService itemService;

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnView() throws Exception {
        ItemDTO item = getItemDTO();
        when(itemService.findByUuid(ITEM_UNIQUE_NUMBER)).thenReturn(item);
        mockMvc.perform(
                get("/items/" + ITEM_UNIQUE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(view().name("item_description"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        ItemDTO item = getItemDTO();
        when(itemService.findByUuid(ITEM_UNIQUE_NUMBER)).thenReturn(item);
        mockMvc.perform(
                get("/items/" + ITEM_UNIQUE_NUMBER)
        ).andExpect(status().isOk());
        verify(itemService, times(1)).findByUuid(ITEM_UNIQUE_NUMBER);
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnItem() throws Exception {
        ItemDTO item = getItemDTO();
        when(itemService.findByUuid(ITEM_UNIQUE_NUMBER)).thenReturn(item);
        mockMvc.perform(
                get("/items/" + ITEM_UNIQUE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(ITEM_UNIQUE_NUMBER)))
                .andExpect(content().string(containsString(ITEM_NAME)));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenUUIDDoesNotExist_returnErrorView() throws Exception {
        mockMvc.perform(
                get("/items/" + ITEM_UNIQUE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("Item not found")));
    }

    private ItemDTO getItemDTO() {
        ItemDTO item = new ItemDTO();
        item.setId(1L);
        item.setUniqueNumber(ITEM_UNIQUE_NUMBER);
        item.setName(ITEM_NAME);
        item.setPrice(BigDecimal.ONE);
        item.setDescription(ITEM_DESCRIPTION);
        return item;
    }

}