package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;

import java.util.EnumMap;
import java.util.function.Supplier;

/**
 * A command that selects and runs one of several commands based on the value
 * of an enum state.
 *
 * @version 1.0
 * @author Kabir Goyal
 */
public class Match<T extends Enum<T>> extends CommandClass {
    private Supplier<T> stateSupplier;
    private T state;
    private EnumMap<T, Command> cases;

    /**
     * Constructs a new Match command that selects a command to run based on
     * the value provided by the state supplier.
     *
     * @param stateSupplier a supplier (function with no parameters) that
     *                      provides the current enum state
     * @param cases         an EnumMap mapping enum branches to commands
     */
    public Match(Supplier<T> stateSupplier, EnumMap<T, Command> cases) {
        this.stateSupplier = stateSupplier;
        this.cases = cases;
    }

    /**
     * Starts the command by evaluating the state supplier and adopting the
     * corresponding behavior.
     * Not to be called directly, use a scheduler instead.
     */
    @Override
    public void start() {
        state = stateSupplier.get();
        if (cases.get(state) != null) {
            adoptBehavior(cases.get(state));
        } else {
            setDone(() -> true);
        }
    }

    /**
     * Creates a copy of this Match command.
     *
     * @return a new Match command with copies of the original commands
     */
    @Override
    public Match<T> copy() {
        EnumMap<T, Command> copiedCases = cases.clone();
        copiedCases.replaceAll((k, v) -> v.copy());
        return new Match<>(stateSupplier, copiedCases);
    }
}
