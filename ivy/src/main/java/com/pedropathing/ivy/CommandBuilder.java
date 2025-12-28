package com.pedropathing.ivy;

import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CommandBuilder implements Command {

    private Set<Object> requirements;
    private int priority;
    private InterruptedBehavior interruptedBehavior;
    private BlockedBehavior blockedBehavior;
    private ConflictBehavior conflictBehavior;
    private Runnable start, execute;
    private BooleanSupplier done;
    private Consumer<EndCondition> end;

    private CommandBuilder(
            Set<Object> requirements,
            int priority,
            InterruptedBehavior interruptedBehavior,
            BlockedBehavior blockedBehavior,
            ConflictBehavior conflictBehavior,
            Runnable start,
            Runnable execute,
            BooleanSupplier done,
            Consumer<EndCondition> end
    ) {
        this.requirements = requirements;
        this.priority = priority;
        this.interruptedBehavior = interruptedBehavior;
        this.blockedBehavior = blockedBehavior;
        this.conflictBehavior = conflictBehavior;
        this.start = start;
        this.execute = execute;
        this.done = done;
        this.end = end;
    }

    public CommandBuilder() {
        this(
                Collections.emptySet(),
                0,
                InterruptedBehavior.END,
                BlockedBehavior.CANCEL,
                ConflictBehavior.OVERRIDE,
                EMPTY_RUNNABLE,
                EMPTY_RUNNABLE,
                EMPTY_BOOLEAN_SUPPLIER,
                EMPTY_END_CONDITION_CONSUMER
        );
    }

    @Override
    public Set<Object> requirements() {
        return requirements;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public InterruptedBehavior interruptedBehavior() {
        return interruptedBehavior;
    }

    @Override
    public ConflictBehavior conflictBehavior() {
        return conflictBehavior;
    }

    @Override
    public BlockedBehavior blockedBehavior() {
        return blockedBehavior;
    }

    @Override
    public final void start() {
        start.run();
    }

    @Override
    public final boolean done() {
        return done.getAsBoolean();
    }

    @Override
    public final void execute() {
        execute.run();
    }

    @Override
    public final void end(EndCondition endCondition) {
        end.accept(endCondition);
    }

    private static final Runnable EMPTY_RUNNABLE = () -> {
    };

    private static final BooleanSupplier EMPTY_BOOLEAN_SUPPLIER = () -> false;

    private static final Consumer<EndCondition> EMPTY_END_CONDITION_CONSUMER = condition -> {
    };


    public CommandBuilder setStart(Runnable start) {
        this.start = start;
        return this;
    }

    public CommandBuilder setExecute(Runnable execute) {
        this.execute = execute;
        return this;
    }

    public CommandBuilder setDone(BooleanSupplier done) {
        this.done = done;
        return this;
    }

    public CommandBuilder setEnd(Consumer<EndCondition> end) {
        this.end = end;
        return this;
    }

    public CommandBuilder requiring(Set<Object> requirements) {
        this.requirements = requirements;
        return this;
    }

    public CommandBuilder requiring(Object... requirements) {
        this.requirements = Arrays.stream(requirements).collect(Collectors.toSet());
        return this;
    }

    public CommandBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public CommandBuilder setInterruptedBehavior(InterruptedBehavior interruptedBehavior) {
        this.interruptedBehavior = interruptedBehavior;
        return this;
    }

    public CommandBuilder setBlockedBehavior(BlockedBehavior blockedBehavior) {
        this.blockedBehavior = blockedBehavior;
        return this;
    }

    public CommandBuilder setConflictBehavior(ConflictBehavior conflictBehavior) {
        this.conflictBehavior = conflictBehavior;
        return this;
    }
}
