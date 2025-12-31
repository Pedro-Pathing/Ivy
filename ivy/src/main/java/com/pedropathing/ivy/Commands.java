package com.pedropathing.ivy;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.commands.Branch;
import com.pedropathing.ivy.commands.Conditional;
import com.pedropathing.ivy.commands.Infinite;
import com.pedropathing.ivy.commands.Instant;
import com.pedropathing.ivy.commands.Lazy;
import com.pedropathing.ivy.commands.Match;
import com.pedropathing.ivy.commands.Wait;
import com.pedropathing.ivy.commands.WaitUntil;
import com.pedropathing.ivy.commands.follow.Follow;
import com.pedropathing.ivy.commands.follow.Hold;
import com.pedropathing.ivy.commands.follow.Turn;
import com.pedropathing.ivy.groups.BooleanLoop;
import com.pedropathing.ivy.groups.Deadline;
import com.pedropathing.ivy.groups.Parallel;
import com.pedropathing.ivy.groups.Race;
import com.pedropathing.ivy.groups.Repeat;
import com.pedropathing.ivy.groups.Sequential;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.PathConstraints;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
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
     * Creates a command that repeatedly runs a command until a condition is true.
     *
     * @param endCondition the condition that stops the loop when true
     * @param command      the command to run repeatedly
     * @return a new BooleanLoop command
     */
    public static CommandBuilder booleanLoop(BooleanSupplier endCondition, Command command) {
        return new BooleanLoop(endCondition, command);
    }

    /**
     * Creates a command that waits for a specified duration.
     *
     * @param milliseconds the time to wait in milliseconds
     * @return a new Wait command
     */
    public static CommandBuilder wait(double milliseconds) {
        return new Wait(milliseconds);
    }

    /**
     * Creates a command that waits until a condition is true.
     *
     * @param condition the condition to wait for
     * @return a new WaitUntil command
     */
    public static CommandBuilder waitUntil(BooleanSupplier condition) {
        return new WaitUntil(condition);
    }

    /**
     * Creates a command that runs indefinitely until interrupted.
     *
     * @param r the runnable to execute each cycle
     * @return a new Infinite command
     */
    public static CommandBuilder infinite(Runnable r) {
        return new Infinite(r);
    }

    /**
     * Creates a command that runs once instantly and completes.
     *
     * @param r the runnable to execute
     * @return a new Instant command
     */
    public static CommandBuilder instant(Runnable r) {
        return new Instant(r);
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

    /**
     * Creates a command that makes the follower follow a path.
     *
     * @param follower  the follower to control
     * @param pathChain the path to follow
     * @return a new Follow command
     */
    public static Follow follow(Follower follower, PathChain pathChain) {
        return new Follow(follower, pathChain);
    }

    /**
     * Creates a command that makes the follower follow a path with specified max power.
     *
     * @param follower  the follower to control
     * @param pathChain the path to follow
     * @param maxPower  the maximum power (between 0 and 1)
     * @return a new Follow command
     */
    public static Follow follow(Follower follower, PathChain pathChain, double maxPower) {
        return new Follow(follower, pathChain, maxPower);
    }

    /**
     * Creates a command that makes the follower follow a path with hold end option.
     *
     * @param follower  the follower to control
     * @param pathChain the path to follow
     * @param holdEnd   whether to hold position at the end
     * @return a new Follow command
     */
    public static Follow follow(Follower follower, PathChain pathChain, boolean holdEnd) {
        return new Follow(follower, pathChain, holdEnd);
    }

    /**
     * Creates a command that makes the follower follow a path with all options.
     *
     * @param follower  the follower to control
     * @param pathChain the path to follow
     * @param holdEnd   whether to hold position at the end
     * @param maxPower  the maximum power (between 0 and 1)
     * @return a new Follow command
     */
    public static Follow follow(Follower follower, PathChain pathChain, boolean holdEnd, double maxPower) {
        return new Follow(follower, pathChain, holdEnd, maxPower);
    }

    /**
     * Creates a command that makes the follower hold its current position.
     *
     * @param follower the follower to control
     * @return a new Hold command
     */
    public static Hold hold(Follower follower) {
        return new Hold(follower);
    }

    /**
     * Creates a command that makes the follower hold a specified pose.
     *
     * @param follower the follower to control
     * @param pose     the pose to hold
     * @return a new Hold command
     */
    public static Hold hold(Follower follower, Pose pose) {
        return new Hold(follower, pose);
    }

    /**
     * Creates a command that makes the follower hold a specified pose with constraints.
     *
     * @param follower    the follower to control
     * @param pose        the pose to hold
     * @param constraints the path constraints for completion tolerance
     * @return a new Hold command
     */
    public static Hold hold(Follower follower, Pose pose, PathConstraints constraints) {
        return new Hold(follower, pose, constraints);
    }

    /**
     * Creates a command that makes the follower turn to a specified heading.
     *
     * @param follower the follower to control
     * @param radians  the target heading in radians
     * @return a new Turn command
     */
    public static Turn turn(Follower follower, double radians) {
        return new Turn(follower, radians);
    }

    /**
     * Creates a command that makes the follower turn to a specified heading with constraints.
     *
     * @param follower    the follower to control
     * @param radians     the target heading in radians
     * @param constraints the path constraints for completion tolerance
     * @return a new Turn command
     */
    public static Turn turn(Follower follower, double radians, PathConstraints constraints) {
        return new Turn(follower, radians, constraints);
    }
}
