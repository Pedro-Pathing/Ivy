package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;

import java.util.Arrays;
import java.util.function.IntSupplier;

import com.pedropathing.ivy.Chainability;

/**
 * A command group that runs a command multiple times in sequence for a
 * specified number iterations.
 * 
 * @version 1.0
 * @author Kabir Goyal
 */
public class Loop extends Sequential {
    IntSupplier iterationsSupplier;
    ICommand command;

    /**
     * Constructs a new Loop command group that runs the given command for the
     * specified number of iterations.
     * 
     * @param command    the command to run in a loop
     * @param iterations the number of times to run the command
     */
    public Loop(ICommand command, int iterations) {
        this(command, () -> iterations);
    }

    /**
     * Constructs a new Loop command group that runs the given command for the
     * specified number of iterations supplied by the given IntSupplier.
     * 
     * @param command            the command to run in a loop
     * @param iterationsSupplier the supplier that provides the number of times to
     *                           run the command
     */
    public Loop(ICommand command, IntSupplier iterationsSupplier) {
        this.command = command;
        this.iterationsSupplier = iterationsSupplier;
    }

    @Override
    public void start() {
        int iterations = iterationsSupplier.getAsInt();
        ICommand[] repeatedCommands = new ICommand[iterations];
        for (int i = 0; i < iterations; i++) {
            repeatedCommands[i] = command.copy();
        }
        commands.addAll(Arrays.asList(repeatedCommands));
        super.start();
    }

    @Override
    public Loop setChainability(Chainability chainability) {
        super.setChainability(chainability);
        return this;
    }

    @Override
    public Loop copy() {
        return new Loop(command.copy(), iterationsSupplier);
    }
}
