package com.gridnine.testing;

import java.util.List;

public interface UserQuery {

    List<Flight> getFlightsWhereDepDateBeforeNow();

    List<Flight> getFlightsWhereSegmentsWithArrDateBeforeDepDate();

    List<Flight> getFlightsWhereOnEarthMoreThan(int hours);

}