package az.edu.turing.booking.controller;

import az.edu.turing.booking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static az.edu.turing.booking.common.FlightTestConstant.PAGE_NUMBER;
import static az.edu.turing.booking.common.FlightTestConstant.PAGE_SIZE;
import static az.edu.turing.booking.common.FlightTestConstant.USER_ID;
import static az.edu.turing.booking.common.JsonFiles.PAGEABLE_USER_RESPONSE;
import static az.edu.turing.booking.common.JsonFiles.USER_RESPONSE;
import static az.edu.turing.booking.common.TestUtils.json;
import static az.edu.turing.booking.common.UserTestConstant.USERNAME;
import static az.edu.turing.booking.common.UserTestConstant.getUserCreateRequest;
import static az.edu.turing.booking.common.UserTestConstant.getUserResponse;
import static az.edu.turing.booking.common.UserTestConstant.getUserResponsePage;
import static az.edu.turing.booking.common.UserTestConstant.getUserStatusUpdateRequest;
import static az.edu.turing.booking.common.UserTestConstant.getUsernameUpdateRequest;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String BASE_URL = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @Autowired
    private UserController userController;

    @Test
    void getByUsername_Should_Return_Success() throws Exception {
        given(userService.getByUsername(USERNAME)).willReturn(getUserResponse());

        String expectedJson = json(USER_RESPONSE);

        mockMvc.perform(get("/api/v1/users/username/{username}", USERNAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        then(userService).should(times(1)).getByUsername(USERNAME);
    }

    @Test
    void getById_Should_Return_Success() throws Exception {
        given(userService.getById(USER_ID)).willReturn(getUserResponse());

        String expectedJson = json(USER_RESPONSE);

        mockMvc.perform(get("/api/v1/users/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        then(userService).should(times(1)).getById(USER_ID);
    }

    @Test
    void deleteById_Should_Return_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

        then(userService).should(times(1)).delete(1L);
    }

    @Test
    void updateStatus_Should_Return_Success() throws Exception {
        given(userService.updateStatus(USER_ID, getUserStatusUpdateRequest()))
                .willReturn(getUserResponse());

        String expectedJson = json(USER_RESPONSE);

        mockMvc.perform(patch(BASE_URL + "/{id}/status", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUserStatusUpdateRequest())))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        then(userService).should(times(1))
                .updateStatus(USER_ID, getUserStatusUpdateRequest());
    }

    @Test
    void updateUsername_Should_Return_Success() throws Exception {
        given(userService.updateUsername(USER_ID, getUsernameUpdateRequest()))
                .willReturn(getUserResponse());

        String expectedJson = json(USER_RESPONSE);

        mockMvc.perform(patch(BASE_URL + "/{id}/username", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUsernameUpdateRequest())))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        then(userService).should(times(1))
                .updateUsername(USER_ID, getUsernameUpdateRequest());
    }

    @Test
    void create_Should_Return_Success() throws Exception {
        given(userService.create(getUserCreateRequest())).willReturn(getUserResponse());

        String expectedJson = json(USER_RESPONSE);

        mockMvc.perform(post(BASE_URL)
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUserCreateRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));

        then(userService).should(times(1))
                .create(getUserCreateRequest());
    }

    @Test
    void getAllUsers_Should_Return_Success() throws Exception {
        given(userService.findAll(PAGE_NUMBER, PAGE_SIZE)).willReturn(getUserResponsePage());

        String expectedJson = json(PAGEABLE_USER_RESPONSE);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        then(userService).should(times(1)).findAll(PAGE_NUMBER, PAGE_SIZE);
    }
}
