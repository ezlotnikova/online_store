package com.gmail.ezlotnikova.web.controller.api;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_DESCRIPTION;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_UNIQUE_NUMBER;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemAPIController.class)
class ItemAPIControllerAddItemTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private ItemService itemService;

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidItem_returnStatusCreated() throws Exception {
        ItemDTO item = getValidItemDTO();
        String content = objectMapper.writeValueAsString(item);
        mockMvc.perform(
                post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidItem_returnBadRequest() throws Exception {
        ItemDTO item = getValidItemDTO();
        item.setName(null);
        String content = objectMapper.writeValueAsString(item);
        mockMvc.perform(
                post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidItem_callBusinessLogic() throws Exception {
        ItemDTO item = getValidItemDTO();
        String content = objectMapper.writeValueAsString(item);
        mockMvc.perform(
                post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isCreated());
        ArgumentCaptor<ItemDTO> itemCaptor = ArgumentCaptor.forClass(ItemDTO.class);
        verify(itemService, times(1)).add(itemCaptor.capture());
        Assertions.assertThat(itemCaptor.getValue().equals(item));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidItem_returnItemWithId() throws Exception {
        ItemDTO item = getValidItemDTO();
        String content = objectMapper.writeValueAsString(item);
        ItemDTO addedItem = getValidItemDTO();
        addedItem.setId(1L);
        when(itemService.add(eq(item))).thenReturn(addedItem);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andReturn();
        String expectedReturnedContent = objectMapper.writeValueAsString(addedItem);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidItem_returnErrorMessage() throws Exception {
        ItemDTO item = getValidItemDTO();
        item.setName(null);
        String content = objectMapper.writeValueAsString(item);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isBadRequest())
                .andReturn();
        APIExceptionHandler.ResponseError responseError = new APIExceptionHandler.ResponseError();
        responseError.setMessage("Is required");
        String expectedReturnedContent = objectMapper.writeValueAsString(responseError);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    private ItemDTO getValidItemDTO() {
        ItemDTO item = new ItemDTO();
        item.setUniqueNumber(ITEM_UNIQUE_NUMBER);
        item.setName(ITEM_NAME);
        item.setPrice(BigDecimal.ONE);
        item.setDescription(ITEM_DESCRIPTION);
        return item;
    }

}