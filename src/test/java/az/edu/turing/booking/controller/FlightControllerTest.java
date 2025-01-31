package az.edu.turing.booking.controller;

import az.edu.turing.booking.exception.AccessDeniedException;
import az.edu.turing.booking.exception.NotFoundException;
import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static az.edu.turing.booking.common.TestConstants.FLIGHT_ID;
import static az.edu.turing.booking.common.TestConstants.USER_ID;
import static az.edu.turing.booking.common.TestConstants.getFlightCreateRequest;
import static az.edu.turing.booking.common.TestConstants.getFlightDetailsResponse;
import static az.edu.turing.booking.common.TestConstants.getFlightResponse;
import static az.edu.turing.booking.common.TestConstants.getFlightResponseWithPage;
import static az.edu.turing.booking.common.TestConstants.getFlightUpdateRequest;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FlightService flightService;

    @Test
    void getAllInNext24Hours_Should_Return_Success() throws Exception {
        Pageable pageable = Pageable.ofSize(20);

        Page<FlightResponse> mockPage = getFlightResponseWithPage(Pageable.ofSize(10));
        given(flightService.getAllInNext24Hours(any(Pageable.class)))
                .willReturn(mockPage);

        mockMvc.perform(get("/api/v1/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].flightId").value(FLIGHT_ID));

        then(flightService).should(times(1)).getAllInNext24Hours(pageable);
    }

    @Test
    void getInfoById_Should_Return_Success() throws Exception {
        FlightDetailsResponse mockResponse = getFlightDetailsResponse();
        given(flightService.getInfoById(FLIGHT_ID)).willReturn(mockResponse);

        mockMvc.perform(get("/api/v1/flights/{flightId}", FLIGHT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departureTime").exists())
                .andExpect(jsonPath("$.arrivalTime").exists());

        then(flightService).should(times(1)).getInfoById(FLIGHT_ID);
    }

    @Test
    void getInfoById_Should_Throw_NotFoundException_When_IdNotFound() throws Exception {
        given(flightService.getInfoById(FLIGHT_ID)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/flights/{flightId}", FLIGHT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void search_Should_Return_Success() throws Exception {
        FlightFilter filter = new FlightFilter();
        Pageable pageable = Pageable.ofSize(10);

        given(flightService.search(filter, pageable)).willReturn(getFlightResponseWithPage(pageable));

        mockMvc.perform(get("/api/v1/flights/search"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(getFlightResponseWithPage(pageable))));

        then(flightService).should(times(1)).search(filter, pageable);
    }

    @Test
    void create_Should_Return_Success() throws Exception {
        given(flightService.create(USER_ID, getFlightCreateRequest())).willReturn(getFlightResponse());

        mockMvc.perform(post("/api/v1/flights")
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getFlightCreateRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(getFlightResponse())));

        then(flightService).should(times(1))
                .create(USER_ID, getFlightCreateRequest());
    }

    @Test
    void create_Should_Throw_NotFoundException_When_UserIsNotAdmin() throws Exception {
        given(flightService.create(USER_ID, getFlightCreateRequest()))
                .willThrow(AccessDeniedException.class);

        mockMvc.perform(post("/api/v1/flights")
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getFlightCreateRequest())))
                .andExpect(status().isForbidden());
    }

    @Test
    void update_Should_Return_Success() throws Exception {

        given(flightService.update(USER_ID, FLIGHT_ID, getFlightUpdateRequest()))
                .willReturn(getFlightResponse());

        mockMvc.perform(put("/api/v1/flights/{flightId}", FLIGHT_ID)
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getFlightUpdateRequest())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(getFlightResponse())));

        then(flightService).should(times(1))
                .update(USER_ID, FLIGHT_ID, getFlightUpdateRequest());
    }

    @Test
    void update_Should_Throw_AccessDeniedException_When_UserIsNotAdmin() throws Exception {
        given(flightService.update(USER_ID, FLIGHT_ID, getFlightUpdateRequest()))
                .willThrow(AccessDeniedException.class);

        mockMvc.perform(put("/api/v1/flights/{flightId}", FLIGHT_ID)
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getFlightUpdateRequest())))
                .andExpect(status().isForbidden());
    }
}
