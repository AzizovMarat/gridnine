package com.gridnine.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class FlightsFilterTest {

    List<Flight> flights;
    FlightsFilter flightsFilter;

    @BeforeEach
    void setUp() {
        flights = FlightBuilder.createFlights();
        flightsFilter = new FlightsFilter(flights);
    }

    @Test
    void getFlightsWhereDepDateBeforeNow() {
        List<Flight> actualFlights = flightsFilter.getFlightsWhereDepDateBeforeNow();

        List<Flight> expectedFlights = List.of(flights.get(2));

        Assertions.assertEquals(expectedFlights, actualFlights);
    }

    @Test
    void getFlightsWhereSegmentsWithArrDateBeforeDepDate() {
        List<Flight> actualFlights = flightsFilter.getFlightsWhereSegmentsWithArrDateBeforeDepDate();

        List<Flight> expectedFlights = List.of(flights.get(3));

        Assertions.assertEquals(expectedFlights, actualFlights);
    }

    @Test
    void getFlightsWhereOnEarthMoreThan() {
        List<Flight> actualFlights = flightsFilter.getFlightsWhereOnEarthMoreThan(2);

        List<Flight> expectedFlights = List.of(flights.get(4), flights.get(5));

        Assertions.assertEquals(expectedFlights, actualFlights);
    }

    @Test
    void executeFlightsWhereDepDateBeforeNow() {
        List<Flight> actualFlights = flightsFilter.execute("get flights dep before now");

        List<Flight> expectedFlights = List.of(flights.get(2));

        Assertions.assertEquals(expectedFlights, actualFlights);
    }

    @Test
    void executeFlightsWhereSegmentsWithArrDateBeforeDepDate() {
        List<Flight> actualFlights = flightsFilter.execute("get segments arr before dep");

        List<Flight> expectedFlights = List.of(flights.get(3));

        Assertions.assertEquals(expectedFlights, actualFlights);
    }

    @Test
    void executeFlightsWhereOnEarthMoreThan() {
        List<Flight> actualFlights = flightsFilter.execute("get flights onearth 2");

        List<Flight> expectedFlights = List.of(flights.get(4), flights.get(5));

        Assertions.assertEquals(expectedFlights, actualFlights);
    }

    @Test
    void executeWrongQuery() {
        Assertions.assertThrows(IllegalArgumentException.class,
                ()-> flightsFilter.execute("get flights onerth 2"));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()-> flightsFilter.execute("get flihts onearth 2"));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()-> flightsFilter.execute("get flights dep before"));
        Assertions.assertThrows(IllegalArgumentException.class,
                ()-> flightsFilter.execute("get flights dep before d"));
    }
}