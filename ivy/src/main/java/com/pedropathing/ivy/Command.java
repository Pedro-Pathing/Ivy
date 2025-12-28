package com.pedropathing.ivy;

import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;

import java.util.Set;

/**
 * A "command" that can be scheduled and composed
 *
 * @author Baron Henderson
 * @author Kabir Goyal
 * @author Davis Luxenberg
 */
public interface Command {
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
     * @param endCondition what caused the command to end
     */
    void end(EndCondition endCondition);

    static CommandBuilder build() {
        return new CommandBuilder();
    }

    default void schedule() {
        Scheduler.schedule(this);
    }

    // TODO: chaining utilities
}
