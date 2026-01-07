package com.pedropathing.ivy;

import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;

import java.util.Arrays;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.*;

/**
 * A "command" that can be scheduled and composed
 *
 * @author Baron Henderson
 * @author Kabir Goyal
 * @author Davis Luxenberg
 */
public interface Command {
    Command NOOP = build();

    static CommandBuilder build() {
        return new CommandBuilder();
    }

    /**
     * The set of requirements the command has
     */
    Set<Object> requirements();

    /**
     * The priority the command has over commands with conflicting requirements
     */
    int priority();

    /**
     * What the command does when interrupted
     */
    InterruptedBehavior interruptedBehavior();

    /**
     * What the command does when a conflicting command with an equal priority is currently running
     */
    ConflictBehavior conflictBehavior();

    /**
     * What the command does when blocked by a command with higher priority
     */
    BlockedBehavior blockedBehavior();

    /**
     * Executed when the command begins running
     */
    void start();

    /**
     * Whether the command has finished executing
     */
    boolean done();

    /**
     * Run repeatedly until the command is completed
     */
    void execute();

    /**
     * Run when the command ends or suspends
     *
     * @param endCondition what caused the command to end
     */
    void end(EndCondition endCondition);

    default void schedule() {
        Scheduler.schedule(this);
    }

    default void cancel() {
        Scheduler.cancel(this);
    }

    default boolean isScheduled() {
        return Scheduler.isScheduled(this);
    }

    default CommandBuilder then(Command... commands) {
        return sequential(Stream.concat(Stream.of(this), Arrays.stream(commands)).toArray(Command[]::new));
    }

    default CommandBuilder with(Command... commands) {
        return parallel(Stream.concat(Stream.of(this), Arrays.stream(commands)).toArray(Command[]::new));
    }

    default CommandBuilder raceWith(Command... commands) {
        return race(Stream.concat(Stream.of(this), Arrays.stream(commands)).toArray(Command[]::new));
    }

    default CommandBuilder until(BooleanSupplier condition) {
        return race(this, waitUntil(condition));
    }

    default CommandBuilder unless(BooleanSupplier condition) {
        return conditional(condition, NOOP, this);
    }

    default CommandBuilder proxy() {
        return build()
                .setStart(this::schedule)
                .setDone(() -> Scheduler.isScheduled(this))
                .setEnd(endCondition -> {
                    if (endCondition == EndCondition.INTERRUPTED) Scheduler.cancel(this);
                });
    }
}
