package az.edu.turing.booking.controller;

import az.edu.turing.booking.model.dto.FlightFilter;
import az.edu.turing.booking.model.dto.request.FlightCreateRequest;
import az.edu.turing.booking.model.dto.request.FlightUpdateRequest;
import az.edu.turing.booking.model.dto.response.FlightDetailsResponse;
import az.edu.turing.booking.model.dto.response.FlightResponse;
import az.edu.turing.booking.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public ResponseEntity<Page<FlightResponse>> getAllInNext24Hours(Pageable pageable) {
        return ResponseEntity.ok(flightService.getAllInNext24Hours(pageable));
    }


    @GetMapping("/{flightId}")
    public ResponseEntity<FlightDetailsResponse> getInfoById(@PathVariable Long flightId) {
        return ResponseEntity.ok(flightService.getInfoById(flightId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FlightResponse>> search(@ParameterObject FlightFilter filter,
                                                       @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(flightService.search(filter, pageable));
    }

    @PostMapping
    public ResponseEntity<FlightResponse> create(@RequestHeader("User-Id") Long userId,
                                                 @Valid @RequestBody FlightCreateRequest flightCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightService.create(userId, flightCreateRequest));
    }

    @PutMapping("/{flightId}")
    public ResponseEntity<FlightResponse> update(@RequestHeader("User-Id") Long userId,
                                                 @PathVariable Long flightId,
                                                 @Valid @RequestBody FlightUpdateRequest flightUpdateRequest) {
        return ResponseEntity.ok(flightService.update(userId, flightId, flightUpdateRequest));
    }
}
