package az.edu.turing.booking.controller;

import az.edu.turing.booking.exception.NotFoundException;
import az.edu.turing.booking.model.dto.BookingDto;
import az.edu.turing.booking.model.dto.request.BookingCreateRequest;

import az.edu.turing.booking.model.enums.BookingStatus;
import az.edu.turing.booking.service.impl.BookingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingServiceImpl bookingService;

    @Test
    void getBookingsByUsernameShouldReturnSuccess() throws Exception {
        given(bookingService.getBookingsByUsername("Ayten")).willReturn(List.of(new BookingDto()));
        mockMvc.perform(get("/api/v1/bookings/users/{username}", "Ayten"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(new BookingDto()))))
                .andDo(MockMvcResultHandlers.print());//why

        then(bookingService).should(times(1)).getBookingsByUsername("Ayten");
    }

    @Test
    void getBookingById_ShouldReturnSuccess() throws Exception {
        BookingDto responseDto = new BookingDto();
        given(bookingService.getBookingById(1L)).willReturn(responseDto);

        mockMvc.perform(get("/api/v1/bookings/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(print());

        then(bookingService).should(times(1)).getBookingById(1L);
    }

    @Test
    void getBookingById_ShouldReturnNotFound_WhenBookingDoesNotExist() throws Exception {
        given(bookingService.getBookingById(anyLong())).willThrow(new NotFoundException("Booking not found"));

        mockMvc.perform(get("/api/v1/bookings/{id}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());

        then(bookingService).should(times(1)).getBookingById(anyLong());
    }

    @Test
    void cancelBooking_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/bookings/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
        then(bookingService).should(times(1)).cancel(1L);
    }

}
