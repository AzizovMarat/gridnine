package com.gridnine.testing;

abstract class Command {
    protected Segment segment;

    abstract Object execute();
}
