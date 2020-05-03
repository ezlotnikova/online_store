package com.gmail.ezlotnikova.web.controller.unit.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.model.ItemPreviewDTO;
import com.gmail.ezlotnikova.web.controller.web.ItemController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_UNIQUE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.OBJECTS_BY_PAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerShowAllTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ItemService itemService;

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnView() throws Exception {
        Page<ItemPreviewDTO> items = getPageOfItems();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(itemService.findPaginatedAndOrderedByName(pageNumber)).thenReturn(items);
        mockMvc.perform(
                get("/items")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(view().name("items"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/items")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        Page<ItemPreviewDTO> items = getPageOfItems();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(itemService.findPaginatedAndOrderedByName(pageNumber)).thenReturn(items);
        mockMvc.perform(
                get("/items")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk());
        verify(itemService, times(1)).findPaginatedAndOrderedByName(pageNumber);
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenValidRequest_returnViewWithItems() throws Exception {
        Page<ItemPreviewDTO> items = getPageOfItems();
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        when(itemService.findPaginatedAndOrderedByName(pageNumber)).thenReturn(items);
        mockMvc.perform(
                get("/items")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(ITEM_UNIQUE_NUMBER)))
                .andExpect(content().string(containsString(ITEM_NAME)));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    void whenInvalidRequest_returnErrorView() throws Exception {
        mockMvc.perform(
                get("/items")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(ARGUMENT_TYPE_MISMATCH_MESSAGE)));
    }

    private Page<ItemPreviewDTO> getPageOfItems() {
        ItemPreviewDTO item = getItemPreviewDTO();
        List<ItemPreviewDTO> itemList = new ArrayList<>();
        itemList.add(item);
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, OBJECTS_BY_PAGE);
        return new PageImpl<>(itemList, pageRequest, 1);
    }

    private ItemPreviewDTO getItemPreviewDTO() {
        ItemPreviewDTO item = new ItemPreviewDTO();
        item.setId(1L);
        item.setUniqueNumber(ITEM_UNIQUE_NUMBER);
        item.setName(ITEM_NAME);
        item.setPrice(BigDecimal.ONE);
        return item;
    }

}