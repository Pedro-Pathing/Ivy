package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Scheduler;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

/**
 * A command that schedules itself when it ends until a given condition is
 * false.
 *
 * @version 1.0
 * @author Kabir Goyal
 */
public class BooleanLoop extends Command {
    private final AtomicReference<Command> repeatedCommandReference = new AtomicReference<>(new Command());
    private final BooleanSupplier endCondition;

    /**
     * Command that schedules itself when it ends until a given condition is false.
     *
     * @param endCondition the condition to check before each iteration
     * @param c            the command to run repeatedly
     */
    public BooleanLoop(BooleanSupplier endCondition, ICommand c) {
        Command repeatedCommand = new Command();
        this.endCondition = endCondition;
        repeatedCommand.adoptBehaviorWithoutStarting(c);
        repeatedCommand.setEnd(
                (interrupted) -> {
                    c.end(interrupted);
                    if (!endCondition.getAsBoolean()) {
                        Scheduler.getInstance().schedule(repeatedCommandReference.get().copy());
                    }
                });
        repeatedCommandReference.set(repeatedCommand);
    }

    /**
     * Starts the first iteration of the lazy command.
     * Not to be called directly, use a scheduler instead.
     */
    @Override
    public void start() {
        Scheduler.getInstance().schedule(repeatedCommandReference.get().copy());
    }

    /**
     * Always returns true, as this command is done immediately after starting.
     *
     * @return true
     */
    @Override
    public boolean done() {
        return endCondition.getAsBoolean();
    }
}
