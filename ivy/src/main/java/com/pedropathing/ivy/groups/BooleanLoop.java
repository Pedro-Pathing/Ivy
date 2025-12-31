package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

import java.util.function.BooleanSupplier;

/**
 * A command group that repeatedly runs a command until a given condition
 * returns true.
 *
 * @version 1.0
 * @author Kabir Goyal
 */
public class BooleanLoop extends CommandBuilder {
    private final Command command;
    private final BooleanSupplier endCondition;

    /**
     * Constructs a new BooleanLoop that runs the given command repeatedly
     * until the end condition returns true.
     *
     * @param endCondition the condition that, when true, stops the loop
     * @param command      the command to run repeatedly
     */
    public BooleanLoop(BooleanSupplier endCondition, Command command) {
        this.command = command;
        this.endCondition = endCondition;

        requiring(command.requirements());

        setPriority(command.priority());

        setStart(() -> {
            if (!done()) {
                command.start();
            }
        });

        setExecute(() -> {
            if (done()) return;

            if (command.done()) {
                command.end(EndCondition.NATURALLY);
                if (!done()) {
                    command.start();
                }
                return;
            }

            command.execute();
        });

        setEnd(endCond -> {
            command.end(endCond);
        });

        setDone(endCondition::getAsBoolean);
    }
}
