package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;

import java.util.function.IntSupplier;

public final class Groups {
    private Groups() {
    }

    /**
     * Creates a command that runs the given commands in sequence.
     *
     * @param commands the commands to run sequentially
     * @return a new Sequential command
     */
    public static CommandBuilder sequential(Command... commands) {
        return new Sequential(commands);
    }

    /**
     * Creates a command that runs the given commands in parallel.
     *
     * @param commands the commands to run in parallel
     * @return a new Parallel command
     */
    public static CommandBuilder parallel(Command... commands) {
        return new Parallel(commands);
    }

    /**
     * Creates a command that runs the given commands in parallel and completes
     * when the first command finishes.
     *
     * @param commands the commands to race
     * @return a new Race command
     */
    public static CommandBuilder race(Command... commands) {
        return new Race(commands);
    }

    /**
     * Creates a command that runs commands in parallel until the deadline command finishes.
     *
     * @param deadline the command that determines when the group ends
     * @param commands the other commands to run in parallel
     * @return a new Deadline command
     */
    public static CommandBuilder deadline(Command deadline, Command... commands) {
        return new Deadline(deadline, commands);
    }

    /**
     * Creates a command that runs the given command a fixed number of times.
     *
     * @param command    the command to repeat
     * @param iterations the number of times to run the command
     * @return a new Repeat command
     */
    public static CommandBuilder repeat(Command command, int iterations) {
        return new Repeat(command, iterations);
    }

    /**
     * Creates a command that runs the given command a dynamic number of times.
     *
     * @param command            the command to repeat
     * @param iterationsSupplier supplies the number of iterations at start time
     * @return a new Repeat command
     */
    public static CommandBuilder repeat(Command command, IntSupplier iterationsSupplier) {
        return new Repeat(command, iterationsSupplier);
    }

    /**
     * Creates a command that runs the given command infinitely until interrupted.
     *
     * @param command the command to loop indefinitely
     * @return a new Loop command
     */
    public static CommandBuilder loop(Command command) {
        return new Loop(command);
    }

}
