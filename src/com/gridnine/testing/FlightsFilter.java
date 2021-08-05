package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlightsFilter implements UserQuery, Query {

    private final List<Flight> flights;

    public FlightsFilter(List<Flight> flights) {
        if (flights != null) {
            this.flights = flights;
        } else {
            throw new IllegalArgumentException("there are no flights");
        }
    }

    @Override
    public List<Flight> getFlightsWhereDepDateBeforeNow() {
        LocalDateTime now = LocalDateTime.now();
        List<Flight> flightsWhereDepDateBeforeNow = new ArrayList<>();

        for (Flight f : flights) {
            if (f.getSegments().get(0).getDepartureDate().isBefore(now)) {
                flightsWhereDepDateBeforeNow.add(f);
            }
        }

        return flightsWhereDepDateBeforeNow;
    }

    @Override
    public List<Flight> getFlightsWhereSegmentsWithArrDateBeforeDepDate() {
        List<Flight> flightsWhereSegmentsWithArrDateBeforeDepDate = new ArrayList<>();

        for (Flight f : flights) {
            for (Segment s : f.getSegments()) {
                if (s.getArrivalDate().isBefore(s.getDepartureDate())) {
                    flightsWhereSegmentsWithArrDateBeforeDepDate.add(f);
                }
            }
        }
        return flightsWhereSegmentsWithArrDateBeforeDepDate;
    }

    @Override
    public List<Flight> getFlightsWhereOnEarthMoreThan(int hours) {
        List<Flight> flightsWhereOnEarthMoreThan = new ArrayList<>();

        Duration duration;

        for (Flight f : flights) {
            long countSeconds = 0;
            List<Segment> segments = f.getSegments();
            for (int i = 0; i < segments.size() - 1; i++) {
                duration = Duration.between(segments.get(i).getArrivalDate(), segments.get(i + 1).getDepartureDate());
                countSeconds += duration.getSeconds();
                if (countSeconds / 3600 > hours) {
                    flightsWhereOnEarthMoreThan.add(f);
                    break;
                }
            }
        }

        return flightsWhereOnEarthMoreThan;
    }

    @Override
    public List<Flight> execute(String query) {
        List<Flight> result = new ArrayList<>();

        Pattern pattern = Pattern.compile("get (flights|segments)( (dep|arr|onearth))( (before|\\d))( (dep|now))?");
        Matcher matcher = pattern.matcher(query);

        String q1, q2, q3;

        if (matcher.find()) {
            q1 = matcher.group(1);
            q2 = matcher.group(3);
            q3 = matcher.group(5);
        } else {
            throw new IllegalArgumentException("wrong query, try one of the following:\n"
            + "get flights dep before now\n"
            + "get segments arr before dep\n"
            + "get flights onearth 2");
        }

        int hours = 0;

        String q4 = null;
        if (q3.equals("before")) {
            if (matcher.groupCount() == 7 && matcher.group(7)  != null) {
                q4 = matcher.group(7);
                Pattern p = Pattern.compile("(dep|now)");
                Matcher m = p.matcher(q4);
                if (!m.find()) {
                    throw new IllegalArgumentException("there is no operator after \"before\"");
                }
            } else {
                throw new IllegalArgumentException("there is no operator after \"before\"");
            }
        } else {
            hours = Integer.parseInt(q3);
        }

        for (Flight f : flights) {
            List<Segment> segments = f.getSegments();
            if (q4 != null) {
                if (q1.equals("flights")) {
                    if (((LocalDateTime) getCurrentValue(segments.get(0), q2)).isBefore((LocalDateTime) getCurrentValue(segments.get(0), q4))) {
                        result.add(f);
                    }
                } else {
                    for (Segment s : segments) {
                        if (((LocalDateTime) getCurrentValue(s, q2)).isBefore((LocalDateTime) getCurrentValue(s, q4))) {
                            result.add(f);
                        }
                    }
                }
            } else {
                Duration duration;
                long countSeconds = 0;
                for (int i = 0; i < segments.size() - 1; i++) {
                    duration = Duration.between((LocalDateTime) getCurrentValue(segments.get(i), "arr"),
                            (LocalDateTime) getCurrentValue(segments.get(i + 1), "dep"));
                    countSeconds += duration.getSeconds();
                    if (countSeconds / 3600 > hours) {
                        result.add(f);
                        break;
                    }
                }
            }
        }

        return result;
    }

    private Object getCurrentValue(Segment segment, String group) {
        Object value = null;
        switch (group) {
            case "dep" -> {
                Command method = new GetDepartureDate(segment);
                value = method.execute();
            }
            case "arr" -> {
                Command method = new GetArrivalDate(segment);
                value = method.execute();
            }
            case "now" -> value = LocalDateTime.now();
        }
        return value;
    }
}