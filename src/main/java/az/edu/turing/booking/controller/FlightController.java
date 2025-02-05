package az.edu.turing.booking.controller;

import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightStatusUpdateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.model.dto.response.PageResponse;
import az.edu.turing.booking.service.FlightService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static az.edu.turing.booking.model.constant.PageConstants.DEFAULT_PAGE_NUMBER;
import static az.edu.turing.booking.model.constant.PageConstants.DEFAULT_PAGE_SIZE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public ResponseEntity<PageResponse<FlightResponse>> getAllInNext24Hours(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) @Min(1) int pageSize) {
        return ResponseEntity.ok(flightService.getAllInNext24Hours(pageNumber, pageSize));
    }


    @GetMapping("/{flightId}")
    public ResponseEntity<FlightDetailsResponse> getInfoById(@PathVariable Long flightId) {
        return ResponseEntity.ok(flightService.getInfoById(flightId));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<FlightResponse>> search(
            @ParameterObject FlightFilter filter,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) @Min(1) int pageSize) {
        return ResponseEntity.ok(flightService.search(filter, pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<FlightResponse> create(@RequestHeader("User-Id") Long userId,
                                                 @Valid @RequestBody FlightCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightService.create(userId, request));
    }

    @PutMapping("/{flightId}")
    public ResponseEntity<FlightResponse> update(@RequestHeader("User-Id") Long userId,
                                                 @PathVariable Long flightId,
                                                 @Valid @RequestBody FlightUpdateRequest request) {
        return ResponseEntity.ok(flightService.update(userId, flightId, request));
    }

    @PatchMapping("/{flightId}")
    public ResponseEntity<FlightResponse> updateStatus(@RequestHeader("User-Id") Long userId,
                                                       @PathVariable Long flightId,
                                                       @Valid @RequestBody FlightStatusUpdateRequest request) {
        return ResponseEntity.ok(flightService.updateStatus(userId, flightId, request));
    }
}
