package com.pedropathing.ivy.bindings;

import com.pedropathing.ivy.Command;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

import static com.pedropathing.ivy.bindings.Bindings.bind;

public final class Binding implements BooleanSupplier {
    private final BooleanSupplier supplier;
    private final Set<Command> riseCommands = new HashSet<>();
    private final Set<Command> fallCommands = new HashSet<>();
    private final Set<Command> riseToFallCommands = new HashSet<>();
    private final Set<Command> fallToRiseCommands = new HashSet<>();
    private final Set<Command> toggleRiseCommands = new HashSet<>();
    private final Set<Command> toggleFallCommands = new HashSet<>();
    private boolean state = false;
    private boolean previousState = false;

    Binding(BooleanSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean getAsBoolean() {
        return getState();
    }

    void update() {
        previousState = state;
        state = supplier.getAsBoolean();

        if (state && !previousState) {
            riseCommands.forEach(Command::schedule);
            riseToFallCommands.forEach(Command::schedule);
            fallToRiseCommands.forEach(Command::cancel);
            toggleRiseCommands.forEach(command -> {
                if (command.isScheduled()) command.cancel();
                else command.schedule();
            });
        }

        if (!state && previousState) {
            fallCommands.forEach(Command::schedule);
            fallToRiseCommands.forEach(Command::schedule);
            riseToFallCommands.forEach(Command::cancel);
            toggleFallCommands.forEach(command -> {
                if (command.isScheduled()) command.cancel();
                else command.schedule();
            });
        }
    }

    public boolean getState() {
        return state;
    }

    public Binding rise(Command command) {
        riseCommands.add(command);
        return this;
    }

    public Binding fall(Command command) {
        fallCommands.add(command);
        return this;
    }

    public Binding riseToFall(Command command) {
        riseToFallCommands.add(command);
        return this;
    }

    public Binding fallToRise(Command command) {
        fallToRiseCommands.add(command);
        return this;
    }

    public Binding toggleRise(Command command) {
        toggleRiseCommands.add(command);
        return this;
    }

    public Binding toggleFall(Command command) {
        toggleFallCommands.add(command);
        return this;
    }

    public Binding and(BooleanSupplier other) {
        return bind(() -> getState() && other.getAsBoolean());
    }

    public Binding or(BooleanSupplier other) {
        return bind(() -> getState() || other.getAsBoolean());
    }

    public Binding not() {
        return bind(() -> !getState());
    }

    public Binding unaryMinus() {
        return not();
    }

    public Binding xor(BooleanSupplier other) {
        return bind(() -> getState() ^ other.getAsBoolean());
    }
}
