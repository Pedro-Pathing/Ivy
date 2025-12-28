package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import java.util.function.BooleanSupplier;

/**
 * A command that chooses between two commands to run based on a boolean
 * condition.
 * 
 * @version 1.0
 * @author Havish Sripada
 * @author Kabir Goyal
 */
public class Conditional extends CommandClass {
    private final BooleanSupplier decider;
    private final Command option1;
    private final Command option2;

    /**
     * Constructs a new Conditional command that runs one of two commands based
     * on the result of the given boolean supplier.
     * 
     * @param decider the boolean supplier (boolean function with no parameters)
     *                that determines which command to run
     * @param ifTrue  the command to run if the decider returns true
     * @param ifFalse the command to run if the decider returns false
     */
    public Conditional(BooleanSupplier decider, Command ifTrue, Command ifFalse) {
        this.decider = decider;
        this.option1 = ifTrue;
        this.option2 = ifFalse;
    }

    /**
     * Starts the command by evaluating the decider and adopting the appropriate
     * behavior.
     * Not to be called directly, use a scheduler instead.
     */
    public void start() {
        if (decider.getAsBoolean()) {
            adoptBehavior(option1);
            return;
        }

        adoptBehavior(option2);
    }

    /**
     * Creates a copy of this Conditional command.
     * 
     * @return a new Conditional command with copies of the original commands
     */
    @Override
    public Conditional copy() {
        return new Conditional(decider, option1.copy(), option2.copy());
    }
}
