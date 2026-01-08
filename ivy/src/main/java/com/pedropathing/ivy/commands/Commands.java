package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.util.Timer;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Factory class providing static methods to create common command types.
 * This class cannot be instantiated.
 *
 * @author Davis Luxenberg
 * @author Kabir Goyal
 * @version 1.0
 */
public final class Commands {

    private Commands() {
        // Prevent instantiation
    }


    /**
     * Creates a command that waits for a specified duration.
     *
     * @param milliseconds the time to wait in milliseconds
     * @return a new Wait command
     */
    public static CommandBuilder waitMs(double milliseconds) {
        Timer timer = new Timer();
        return Command.build()
                .setStart(timer::resetTimer)
                .setDone(() -> timer.getElapsedTime() >= milliseconds);
    }

    /**
     * Creates a command that waits until a condition is true.
     *
     * @param condition the condition to wait for
     * @return a new WaitUntil command
     */
    public static CommandBuilder waitUntil(BooleanSupplier condition) {
        return Command.build().setDone(condition);
    }

    /**
     * Creates a command that runs indefinitely until interrupted.
     *
     * @param r the runnable to execute each cycle
     * @return a new Infinite command
     */
    public static CommandBuilder infinite(Runnable r) {
        return Command.build()
                .setExecute(r)
                .setDone(() -> false);
    }

    /**
     * Creates a command that runs once instantly and completes.
     *
     * @param r the runnable to execute
     * @return a new Instant command
     */
    public static CommandBuilder instant(Runnable r) {
        return Command.build()
                .setStart(r)
                .setDone(() -> true);
    }

    /**
     * Creates a command that chooses between two commands based on a condition.
     *
     * @param decider the condition that determines which command to run
     * @param ifTrue  the command to run if the condition is true
     * @param ifFalse the command to run if the condition is false
     * @return a new Conditional command
     */
    public static CommandBuilder conditional(BooleanSupplier decider, Command ifTrue, Command ifFalse) {
        return new Conditional(decider, ifTrue, ifFalse);
    }

    /**
     * Creates a command that chooses between multiple commands based on conditions.
     *
     * @param commands a map of conditions to commands (first matching condition wins)
     * @return a new Branch command
     */
    public static CommandBuilder branch(LinkedHashMap<BooleanSupplier, Command> commands) {
        return new Branch(commands);
    }

    /**
     * Creates a command that defers command creation until start time.
     *
     * @param commandSupplier the supplier that creates the command when started
     * @return a new Lazy command
     */
    public static CommandBuilder lazy(Supplier<Command> commandSupplier) {
        return new Lazy(commandSupplier);
    }

    /**
     * Creates a command that selects a command based on an enum state.
     *
     * @param stateSupplier supplies the enum state at start time
     * @param cases         a map of enum values to commands
     * @param <T>           the enum type
     * @return a new Match command
     */
    public static <T extends Enum<T>> CommandBuilder match(Supplier<T> stateSupplier, EnumMap<T, Command> cases) {
        return new Match<>(stateSupplier, cases);
    }

    public static CommandBuilder onInterrupt(Runnable callback) {
        return Command.build()
                .setDone(() -> false)
                .setEnd(_endCondition -> callback.run());
    }
}
