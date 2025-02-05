package az.edu.turing.booking.service;

import az.edu.turing.booking.domain.entity.FlightEntity;
import az.edu.turing.booking.domain.entity.UserEntity;
import az.edu.turing.booking.domain.repository.FlightDetailsRepository;
import az.edu.turing.booking.domain.repository.FlightRepository;
import az.edu.turing.booking.domain.repository.FlightSpecification;
import az.edu.turing.booking.domain.repository.UserRepository;
import az.edu.turing.booking.exception.BaseException;
import az.edu.turing.booking.mapper.FlightMapper;
import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.model.enums.UserRole;
import az.edu.turing.booking.service.impl.FlightServiceImpl;
import az.edu.turing.booking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static az.edu.turing.booking.common.FlightTestConstant.FLIGHT_ID;
import static az.edu.turing.booking.common.FlightTestConstant.PAGE_NUMBER;
import static az.edu.turing.booking.common.FlightTestConstant.PAGE_SIZE;
import static az.edu.turing.booking.common.FlightTestConstant.USER_ID;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightCreateRequest;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightDetailsEntity;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightEntity;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightEntityWithPage;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightResponseWithPage;
import static az.edu.turing.booking.common.FlightTestConstant.getFlightUpdateRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Spy
    private FlightMapper flightMapper = FlightMapper.INSTANCE;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightSpecification flightSpecification;

    @Mock
    private FlightDetailsRepository flightDetailsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private FlightServiceImpl flightService;

    @Test
    void getAllInNext24Hours_Should_Return_Success() {
        Pageable pageable = Pageable.ofSize(20);
        given(flightRepository.findByDepartureTimeBetweenAndStatusIs(any(), any(),
                any(), any()))
                .willReturn(getFlightEntityWithPage(pageable));

//        Page<FlightResponse> result = flightService.getAllInNext24Hours(PAGE_NUMBER, PAGE_SIZE);

//        Assertions.assertNotNull(result);
        then(flightRepository).should(times(1))
                .findByDepartureTimeBetweenAndStatusIs(any(), any(), any(), any());
    }

    @Test
    void create_Should_Return_Success() {
        UserEntity adminUser = new UserEntity();
        adminUser.setId(USER_ID);
        adminUser.setRole(UserRole.ADMIN);

        given(userService.isAdmin(USER_ID)).willReturn(true);

        FlightEntity flight = getFlightEntity();
        given(flightRepository.save(any(FlightEntity.class))).willReturn(flight);

        FlightResponse response = flightService.create(USER_ID, getFlightCreateRequest());

        Assertions.assertNotNull(response);
        then(userService).should(times(1)).isAdmin(USER_ID);
    }

    @Test
    void create_Should_Throw_AccessDeniedException_WhenUserIsNotAdmin() {
        BaseException ex = Assertions.assertThrows(BaseException.class,
                () -> flightService.create(USER_ID, getFlightCreateRequest()));

        Assertions.assertEquals("Access Denied", ex.getMessage());
        then(flightRepository).should(never()).save(any());
    }

    @Test
    void update_Should_Return_Success() {
        UserEntity adminUser = new UserEntity();
        adminUser.setId(USER_ID);
        adminUser.setRole(UserRole.ADMIN);

        FlightEntity flight = getFlightEntity();
        flight.setId(FLIGHT_ID);

        FlightUpdateRequest updateRequest = getFlightUpdateRequest();

        given(userService.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.save(any(FlightEntity.class))).willReturn(flight);
        given(flightRepository.findById(FLIGHT_ID)).willReturn(Optional.of(flight));
        given(flightDetailsRepository.findById(FLIGHT_ID)).willReturn(Optional.of(flight.getFlightDetail()));

        FlightResponse response = flightService.update(USER_ID, FLIGHT_ID, updateRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(FLIGHT_ID, response.getFlightId());

        then(userService).should(times(1)).isAdmin(USER_ID);
        then(flightRepository).should(times(1)).save(any(FlightEntity.class));
    }


    @Test
    void update_Should_Throw_NotFoundException_WhenIdNotFound() {
        UserEntity adminUser = new UserEntity();
        adminUser.setId(USER_ID);
        adminUser.setRole(UserRole.ADMIN);

        given(userService.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.findById(FLIGHT_ID)).willReturn(Optional.empty());

        BaseException ex = Assertions.assertThrows(BaseException.class,
                () -> flightService.update(USER_ID, FLIGHT_ID, getFlightUpdateRequest()));

        Assertions.assertEquals("Flight not found", ex.getMessage());
    }

    @Test
    void update_Should_Throw_AccessDeniedException_WhenUserIsNotAdmin() {
        BaseException ex = Assertions.assertThrows(BaseException.class,
                () -> flightService.update(USER_ID, FLIGHT_ID, getFlightUpdateRequest()));

        Assertions.assertEquals("Access Denied", ex.getMessage());
    }

    @Test
    void getInfoById_Should_Return_Success() {
        FlightEntity flight = getFlightEntity();
        flight.setFlightDetail(getFlightDetailsEntity());
        given(flightRepository.findById(FLIGHT_ID)).willReturn(Optional.of(flight));

        FlightDetailsResponse response = flightService.getInfoById(FLIGHT_ID);
        Assertions.assertNotNull(response);
        then(flightRepository).should(times(1)).findById(FLIGHT_ID);
    }

    @Test
    void getInfoById_Should_Throw_NotFoundException_WhenIdNotFound() {
        BaseException ex = Assertions.assertThrows(BaseException.class,
                () -> flightService.getInfoById(FLIGHT_ID));

        Assertions.assertEquals("Flight not found", ex.getMessage());

        then(flightRepository).should(times(1)).findById(FLIGHT_ID);
    }

    @Test
    void search_Should_Return_Success() {
        Pageable pageable = Pageable.ofSize(10);
        FlightFilter filter = new FlightFilter();

        given(flightRepository.findAll(any(Specification.class), eq(pageable)))
                .willReturn(new PageImpl<>(List.of(getFlightEntity())));

        try (MockedStatic<FlightSpecification> flightSpecMock = Mockito.mockStatic(FlightSpecification.class)) {
            Specification<FlightEntity> specMock = mock(Specification.class);

            flightSpecMock.when(() -> FlightSpecification.hasOriginPoints(filter.getOriginPoints())).thenReturn(specMock);
            flightSpecMock.when(() -> FlightSpecification.hasDestinationPoints(filter.getDestinationPoints())).thenReturn(specMock);
            flightSpecMock.when(() -> FlightSpecification.hasDepartureTimeBetween(any(), any())).thenReturn(specMock);
            flightSpecMock.when(() -> FlightSpecification.hasPriceBetween(any(), any())).thenReturn(specMock);

//            Page<FlightResponse> response = flightService.search(filter, PAGE_NUMBER, PAGE_SIZE);

//            Assertions.assertNotNull(response);
//            Assertions.assertEquals(getFlightResponseWithPage(pageable).getContent(), response.getContent());
        }

        then(flightRepository).should(times(1)).findAll(any(Specification.class), eq(pageable));
    }
}
