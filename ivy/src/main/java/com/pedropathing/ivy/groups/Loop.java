package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import com.pedropathing.ivy.Chainability;

/**
 * A command group that runs a command multiple times in sequence for a
 * specified number iterations.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public class Loop extends Sequential {
    /**
     * Constructs a new Loop command group that runs the given command for the
     * specified number of iterations.
     * 
     * @param command    the command to run in a loop
     * @param iterations the number of times to run the command
     */
    public Loop(ICommand command, int iterations) {
        ICommand[] loops = new ICommand[iterations];
        for (int i = 0; i < iterations; i++) {
            loops[i] = command.copy();
        }
        commands.addAll(Arrays.asList(loops));
        rebuildRequirements();
        generateInterruptibility();
    }

    /**
     * Constructs a new Loop command group that runs the given command
     * indefinitely until interrupted.
     * 
     * @param command the command to run in a loop
     */
    @Override
    public Loop setChainability(Chainability chainability) {
        super.setChainability(chainability);
        return this;
    }
}
