package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.ICommand;

import java.util.EnumMap;
import java.util.function.Supplier;

public class Match<T extends Enum<T>> extends Command {
    private Supplier<T> stateSupplier;
    private T state;
    private EnumMap<T, ICommand> cases;

    public Match(Supplier<T> stateSupplier, EnumMap<T, ICommand> cases) {
        this.stateSupplier = stateSupplier;
        this.cases = cases;
    }

    @Override
    public void start() {
        state = stateSupplier.get();
        if (cases.get(state) != null) {
            adoptBehavior(cases.get(state));
        } else {
            setDone(() -> true);
        }
    }

    @Override
    public Match<T> copy() {
        EnumMap<T, ICommand> copiedCases = cases.clone();
        copiedCases.replaceAll((k, v) -> v.copy());
        return new Match<>(stateSupplier, copiedCases);
    }
}
