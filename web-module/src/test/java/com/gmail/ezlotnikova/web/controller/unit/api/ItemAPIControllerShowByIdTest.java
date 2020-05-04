package com.gmail.ezlotnikova.web.controller.unit.api;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.model.ItemDTO;
import com.gmail.ezlotnikova.web.controller.api.APIExceptionHandler;
import com.gmail.ezlotnikova.web.controller.api.ItemAPIController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_DESCRIPTION;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_NAME;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.ITEM_UNIQUE_NUMBER;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemAPIController.class)
public class ItemAPIControllerShowByIdTest {

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
                get("/api/items/" + VALID_ID)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/api/items/" + INVALID_ID)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        mockMvc.perform(
                get("/api/items/" + VALID_ID)
        ).andExpect(status().isOk());
        Long id = Long.parseLong(VALID_ID);
        verify(itemService, times(1)).findById(id);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_returnItem() throws Exception {
        ItemDTO item = getItemDTO();
        Long id = Long.parseLong(VALID_ID);
        when(itemService.findById(id)).thenReturn(item);

        MvcResult mvcResult = mockMvc.perform(
                get("/api/items/" + VALID_ID)
        ).andReturn();

        String expectedReturnedContent = objectMapper.writeValueAsString(item);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnErrorMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/items/" + INVALID_ID)
        ).andReturn();
        APIExceptionHandler.ResponseError responseError = new APIExceptionHandler.ResponseError();
        responseError.setMessage(ARGUMENT_TYPE_MISMATCH_MESSAGE);
        String expectedReturnedContent = objectMapper.writeValueAsString(responseError);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
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