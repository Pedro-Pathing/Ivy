package com.pedropathing.ivy;

import com.pedropathing.ivy.groups.Deadline;
import com.pedropathing.ivy.groups.Parallel;
import com.pedropathing.ivy.groups.Race;
import com.pedropathing.ivy.groups.Sequential;

import java.util.function.IntSupplier;

public final class Commands {
    public static CommandBuilder sequential(Command... commands) {
        return new Sequential(commands);
    }

    public static CommandBuilder parallel(Command... commands) {
        return new Parallel(commands);
    }

    public static CommandBuilder race(Command... commands) {
        return new Race(commands);
    }

    public static CommandBuilder deadline(Command deadline, Command... commands) {
        return new Deadline(deadline, commands);
    }

    public static CommandBuilder loop(Command command, int iterations) {
        return new com.pedropathing.ivy.groups.Loop(command, iterations);
    }

    public static CommandBuilder loop(Command command, IntSupplier iterationsSupplier) {
        return new com.pedropathing.ivy.groups.Loop(command, iterationsSupplier);
    }
}
