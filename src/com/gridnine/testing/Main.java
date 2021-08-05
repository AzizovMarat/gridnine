package com.gridnine.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Flight> flights = FlightBuilder.createFlights();

        FlightsFilter flightsFilter = new FlightsFilter(flights);
        System.out.println("Реализовал два способа решения задачи.\n" +
                "Первый - путём реализации методов по каждому запросу:");
        System.out.println("1). вылет до текущего момента времени");
        System.out.println(flightsFilter.getFlightsWhereDepDateBeforeNow());

        System.out.println("2). имеются сегменты с датой прилёта раньше даты вылета");
        System.out.println(flightsFilter.getFlightsWhereSegmentsWithArrDateBeforeDepDate());

        System.out.println("3). общее время, проведённое на земле превышает два часа (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)");
        System.out.println(flightsFilter.getFlightsWhereOnEarthMoreThan(2));

        System.out.println("Второй - путём реализации языка запросов:");
        String query = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Введите запрос (например: 'get flights dep before now' или 'get segments arr before dep' или 'get flights onearth 2')");
            query = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(flightsFilter.execute(query));
    }
}
