package com.gmail.ezlotnikova.web.controller.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.model.ItemPreviewDTO;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_UNIQUE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.OBJECTS_BY_PAGE;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_PAGE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemAPIController.class)
public class ItemAPIControllerShowAllTest {

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
    void whenValidRequest_returnStatusOk() throws Exception {
        mockMvc.perform(
                get("/api/items")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/api/items")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        mockMvc.perform(
                get("/api/items")
                        .param("page", VALID_PAGE_NUMBER)
        ).andExpect(status().isOk());
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        verify(itemService, times(1)).findPaginatedAndOrderedByName(pageNumber);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_returnPageOfItems() throws Exception {
        ItemPreviewDTO item = getItemPreviewDTO();
        List<ItemPreviewDTO> itemList = new ArrayList<>();
        itemList.add(item);
        int pageNumber = Integer.parseInt(VALID_PAGE_NUMBER);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, OBJECTS_BY_PAGE);
        Page<ItemPreviewDTO> items = new PageImpl<>(itemList, pageRequest, 1);
        when(itemService.findPaginatedAndOrderedByName(pageNumber)).thenReturn(items);

        MvcResult mvcResult = mockMvc.perform(
                get("/api/items")
                        .param("page", VALID_PAGE_NUMBER)
        ).andReturn();

        String expectedReturnedContent = objectMapper.writeValueAsString(items);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnErrorMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/items")
                        .param("page", INVALID_PAGE_NUMBER)
        ).andReturn();
        APIExceptionHandler.ResponseError responseError = new APIExceptionHandler.ResponseError();
        responseError.setMessage(ARGUMENT_TYPE_MISMATCH_MESSAGE);
        String expectedReturnedContent = objectMapper.writeValueAsString(responseError);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
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