package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * A command that chooses between two commands to run based on a boolean
 * condition.
 *
 * @version 1.0
 * @author Havish Sripada
 * @author Kabir Goyal
 */
public class Conditional extends CommandBuilder {
    private final BooleanSupplier decider;
    private final Command ifTrue;
    private final Command ifFalse;
    private Command selected;

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
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;

        Set<Object> allRequirements = new HashSet<>();
        allRequirements.addAll(ifTrue.requirements());
        allRequirements.addAll(ifFalse.requirements());
        requiring(allRequirements);

        setPriority(Math.max(ifTrue.priority(), ifFalse.priority()));

        setStart(() -> {
            selected = decider.getAsBoolean() ? ifTrue : ifFalse;
            selected.start();
        });

        setExecute(() -> {
            if (done()) return;
            selected.execute();
        });

        setDone(() -> selected != null && selected.done());

        setEnd(endCondition -> {
            if (selected != null) {
                selected.end(endCondition);
            }
        });
    }
}
