package com.gmail.ezlotnikova.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.ezlotnikova.service.ItemService;
import com.gmail.ezlotnikova.service.constant.ExecutionResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.ezlotnikova.service.constant.ErrorCodeConstant.NO_OBJECT_FOUND;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.INVALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ControllerTestConstant.VALID_ID;
import static com.gmail.ezlotnikova.web.controller.constant.ExceptionHandlerConstant.ARGUMENT_TYPE_MISMATCH_MESSAGE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemAPIController.class)
public class ItemAPIControllerDeleteByIdTest {

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
                delete("/api/items/" + VALID_ID)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnBadRequest() throws Exception {
        mockMvc.perform(
                delete("/api/items/" + INVALID_ID)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_callBusinessLogic() throws Exception {
        mockMvc.perform(
                delete("/api/items/" + VALID_ID)
        ).andExpect(status().isOk());
        Long id = Long.parseLong(VALID_ID);
        verify(itemService, times(1)).deleteItemById(id);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenValidRequest_deleteItem() throws Exception {
        ExecutionResult result = ExecutionResult.ok();
        Long id = Long.parseLong(VALID_ID);
        ExecutionResult operationResult = ExecutionResult.ok();
        when(itemService.deleteItemById(id)).thenReturn(operationResult);

        MvcResult mvcResult = mockMvc.perform(
                delete("/api/items/" + VALID_ID)
        ).andReturn();

        String expectedReturnedContent = objectMapper.writeValueAsString(result);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenIdDoesNotExist_returnMessage() throws Exception {
        Long id = Long.parseLong(VALID_ID);
        ExecutionResult result = ExecutionResult.error(NO_OBJECT_FOUND, "No item found with id provided");
        ExecutionResult operationResult = ExecutionResult.error(NO_OBJECT_FOUND, "No item found with id provided");
        when(itemService.deleteItemById(id)).thenReturn(operationResult);

        MvcResult mvcResult = mockMvc.perform(
                delete("/api/items/" + VALID_ID)
        ).andReturn();

        String expectedReturnedContent = objectMapper.writeValueAsString(result);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    void whenInvalidRequest_returnErrorMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                delete("/api/items/" + INVALID_ID)
        ).andReturn();
        APIExceptionHandler.ResponseError responseError = new APIExceptionHandler.ResponseError();
        responseError.setMessage(ARGUMENT_TYPE_MISMATCH_MESSAGE);
        String expectedReturnedContent = objectMapper.writeValueAsString(responseError);
        String actualReturnedContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualReturnedContent).isEqualTo(expectedReturnedContent);
    }

}