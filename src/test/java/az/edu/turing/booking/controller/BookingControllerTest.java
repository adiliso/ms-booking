package az.edu.turing.booking.controller;

import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.service.impl.BookingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static az.edu.turing.booking.common.BookingTestConstants.BOOKING_ID;
import static az.edu.turing.booking.common.BookingTestConstants.STATUS;
import static az.edu.turing.booking.common.BookingTestConstants.USERNAME;
import static az.edu.turing.booking.common.BookingTestConstants.USER_ID;
import static az.edu.turing.booking.common.BookingTestConstants.getBookingCreateRequest;
import static az.edu.turing.booking.common.BookingTestConstants.getBookingDto;
import static az.edu.turing.booking.common.BookingTestConstants.getBookingUpdateRequest;
import static az.edu.turing.booking.common.JsonFiles.BOOKING_DTO;
import static az.edu.turing.booking.common.JsonFiles.LIST_BOOKING_DTO;
import static az.edu.turing.booking.common.TestUtils.json;
import static az.edu.turing.booking.model.enums.ErrorEnum.ACCESS_DENIED;
import static az.edu.turing.booking.model.enums.ErrorEnum.BOOKING_NOT_FOUND;
import static az.edu.turing.booking.model.enums.ErrorEnum.INVALID_OPERATION;
import static az.edu.turing.booking.model.enums.ErrorEnum.USER_NOT_FOUND;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    private static final String BASE_URL = "/api/v1/bookings";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingServiceImpl bookingService;

    @Test
    void create_Should_Return_Success() throws Exception {
        given(bookingService.create(USER_ID, getBookingCreateRequest())).willReturn(getBookingDto());

        mockMvc.perform(post(BASE_URL)
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBookingCreateRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().json(json(BOOKING_DTO)));

        then(bookingService).should(times(1)).create(USER_ID, getBookingCreateRequest());
    }

    @Test
    void create_Should_Throw_Exception_When_UserNotFound() throws Exception {
        given(bookingService.create(USER_ID, getBookingCreateRequest())).willThrow(new BaseException(USER_NOT_FOUND));

        mockMvc.perform(post(BASE_URL)
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBookingCreateRequest())))
                .andExpect(status().isNotFound());

        then(bookingService).should(times(1)).create(USER_ID, getBookingCreateRequest());
    }

    @Test
    void create_Should_Throw_Exception_When_InvalidOperation() throws Exception {
        given(bookingService.create(USER_ID, getBookingCreateRequest()))
                .willThrow(new BaseException(INVALID_OPERATION));

        mockMvc.perform(post(BASE_URL)
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBookingCreateRequest())))
                .andExpect(status().isMethodNotAllowed());

        then(bookingService).should(times(1)).create(USER_ID, getBookingCreateRequest());
    }

    @Test
    void updateById_Should_Return_Success() throws Exception {
        given(bookingService.update(USER_ID, BOOKING_ID, getBookingUpdateRequest())).willReturn(getBookingDto());

        mockMvc.perform(put(BASE_URL + "/{bookingId}", BOOKING_ID)
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBookingUpdateRequest())))
                .andExpect(status().isOk())
                .andExpect(content().json(json(BOOKING_DTO)));

        then(bookingService).should(times(1))
                .update(USER_ID, BOOKING_ID, getBookingUpdateRequest());
    }

    @Test
    void getByUsername_Should_Return_Success() throws Exception {
        given(bookingService.getBookingsByUsername(USERNAME)).willReturn(Set.of(getBookingDto()));

        mockMvc.perform(get(BASE_URL + "/users/{username}", USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().json(json(LIST_BOOKING_DTO)));

        then(bookingService).should(times(1)).getBookingsByUsername(USERNAME);
    }

    @Test
    void getByUsername_Should_Throw_Exception_When_UserNotFound() throws Exception {
        given(bookingService.getBookingsByUsername(USERNAME)).willThrow(new BaseException(USER_NOT_FOUND));

        mockMvc.perform(get(BASE_URL + "/users/{username}", USERNAME))
                .andExpect(status().isNotFound());

        then(bookingService).should(times(1)).getBookingsByUsername(USERNAME);
    }

    @Test
    void getById_Should_Return_Success() throws Exception {
        given(bookingService.getBookingById(BOOKING_ID)).willReturn(getBookingDto());

        mockMvc.perform(get(BASE_URL + "/{id}", BOOKING_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(json(BOOKING_DTO)));

        then(bookingService).should(times(1)).getBookingById(BOOKING_ID);
    }

    @Test
    void getById_Should_Return_Exception_When_BookingNotFound() throws Exception {
        given(bookingService.getBookingById(BOOKING_ID)).willThrow(new BaseException(BOOKING_NOT_FOUND));

        mockMvc.perform(get(BASE_URL + "/{id}", BOOKING_ID))
                .andExpect(status().isNotFound());

        then(bookingService).should(times(1)).getBookingById(BOOKING_ID);
    }

    @Test
    void updateStatus_Should_Return_Success() throws Exception {
        given(bookingService.updateStatus(USER_ID, BOOKING_ID, STATUS)).willReturn(getBookingDto());

        mockMvc.perform(patch(BASE_URL + "/{id}/status", BOOKING_ID)
                        .header("User-Id", USER_ID)
                        .param("status", STATUS.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(json(BOOKING_DTO)));

        then(bookingService).should(times(1)).updateStatus(USER_ID, BOOKING_ID, STATUS);
    }

    @Test
    void updateStatus_Should_Throw_Exception_When_AccessDenied() throws Exception {
        given(bookingService.updateStatus(USER_ID, BOOKING_ID, STATUS)).willThrow(new BaseException(ACCESS_DENIED));

        mockMvc.perform(patch(BASE_URL + "/{id}/status", BOOKING_ID)
                        .header("User-Id", USER_ID)
                        .param("status", STATUS.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        then(bookingService).should(times(1)).updateStatus(USER_ID, BOOKING_ID, STATUS);
    }

    @Test
    void updateStatus_Should_Throw_Exception_When_BookingNotFound() throws Exception {
        given(bookingService.updateStatus(USER_ID, BOOKING_ID, STATUS)).willThrow(new BaseException(BOOKING_NOT_FOUND));

        mockMvc.perform(patch(BASE_URL + "/{id}/status", BOOKING_ID)
                        .header("User-Id", USER_ID)
                        .param("status", STATUS.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        then(bookingService).should(times(1)).updateStatus(USER_ID, BOOKING_ID, STATUS);
    }

    @Test
    void cancel_Should_Return_NoContent() throws Exception {
        doNothing().when(bookingService).cancel(USER_ID, BOOKING_ID);

        mockMvc.perform(delete(BASE_URL + "/{id}", BOOKING_ID)
                        .header("User-Id", USER_ID))
                .andExpect(status().isNoContent());

        then(bookingService).should(times(1)).cancel(USER_ID, BOOKING_ID);
    }

    @Test
    void cancel_Should_Throw_AccessDenied() throws Exception {
        doThrow(new BaseException(ACCESS_DENIED)).when(bookingService).cancel(USER_ID, BOOKING_ID);

        mockMvc.perform(delete(BASE_URL + "/{id}", BOOKING_ID)
                        .header("User-Id", USER_ID))
                .andExpect(status().isForbidden());

        then(bookingService).should(times(1)).cancel(USER_ID, BOOKING_ID);
    }

    @Test
    void cancel_Should_Throw_BookingNotFound() throws Exception {
        doThrow(new BaseException(BOOKING_NOT_FOUND)).when(bookingService).cancel(USER_ID, BOOKING_ID);

        mockMvc.perform(delete(BASE_URL + "/{id}", BOOKING_ID)
                        .header("User-Id", USER_ID))
                .andExpect(status().isNotFound());

        then(bookingService).should(times(1)).cancel(USER_ID, BOOKING_ID);
    }
}
