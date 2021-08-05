package com.gridnine.testing;

class GetDepartureDate extends Command {
    GetDepartureDate(Segment segment) {
        this.segment = segment;
    }

    @Override
    Object execute() {
        return segment.getDepartureDate();
    }
}
