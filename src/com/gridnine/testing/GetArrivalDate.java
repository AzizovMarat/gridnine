package com.gridnine.testing;

class GetArrivalDate extends Command {
    GetArrivalDate(Segment segment) {
        this.segment = segment;
    }

    @Override
    Object execute() {
        return segment.getArrivalDate();
    }
}
