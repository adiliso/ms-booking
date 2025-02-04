package az.edu.turing.booking.controller;

import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.model.enums.ErrorEnum;
import az.edu.turing.booking.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static az.edu.turing.booking.common.FlightTestConstant.FLIGHT_ID;
import static az.edu.turing.booking.common.FlightTestConstant.PAGE_NUMBER;
import static az.edu.turing.booking.common.FlightTestConstant.PAGE_SIZE;
import static az.edu.turing.booking.common.FlightTestConstant.USER_ID;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightCreateRequest;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightDetailsResponse;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightResponse;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightResponseWithPage;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightUpdateRequest;
import static org.hamcrest.Matchers.hasSize;
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

        Page<FlightResponse> mockPage =
                getFlightResponseWithPage(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        given(flightService.getAllInNext24Hours(
                PAGE_NUMBER, PAGE_SIZE)).willReturn(mockPage);
        mockMvc.perform(get("/api/v1/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].flightId").value(FLIGHT_ID));

        then(flightService).should(times(1))
                .getAllInNext24Hours(PAGE_NUMBER, PAGE_SIZE);
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
        given(flightService.getInfoById(FLIGHT_ID))
                .willThrow(new BaseException(ErrorEnum.FLIGHT_DETAILS_NOT_FOUND));

        mockMvc.perform(get("/api/v1/flights/{flightId}", FLIGHT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void search_Should_Return_Success() throws Exception {
        FlightFilter filter = new FlightFilter();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        given(flightService.search(filter, PAGE_NUMBER, PAGE_SIZE))
                .willReturn(getFlightResponseWithPage(pageable));

        mockMvc.perform(get("/api/v1/flights/search"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(getFlightResponseWithPage(pageable))));

        then(flightService).should(times(1))
                .search(filter, PAGE_NUMBER, PAGE_SIZE);
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
                .willThrow(new BaseException(ErrorEnum.ACCESS_DENIED));

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
                .willThrow(new BaseException(ErrorEnum.ACCESS_DENIED));

        mockMvc.perform(put("/api/v1/flights/{flightId}", FLIGHT_ID)
                        .header("User-Id", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getFlightUpdateRequest())))
                .andExpect(status().isForbidden());
    }
}
