package com.gridnine.testing;

import java.util.List;

public interface Query {

    List<Flight> execute(String query);

}
