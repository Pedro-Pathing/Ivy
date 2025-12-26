package com.pedropathing.ivy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class Command implements ICommand {
    private List<Object> requirements = new ArrayList<>();
    private Runnable execute, start;
    private Consumer<Boolean> end;
    private BooleanSupplier done;
    private Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;
    private Chainability chainability = Chainability.UNCHAINABLE;

    public void execute() {
        if (execute != null) {
            execute.run();
        }
    }

    public void start() {
        if (start != null) {
            start.run();
        }
    }

    public void end(boolean interrupted) {
        if (end != null) {
            end.accept(interrupted);
        }
    }

    public boolean done() {
        if (done != null) {
            return done.getAsBoolean();
        }
        return false;
    }

    public Command setExecute(Runnable r) {
        this.execute = r;
        return this;
    }

    public Command setStart(Runnable r) {
        this.start = r;
        return this;
    }

    public Command setEnd(Consumer<Boolean> r) {
        this.end = r;
        return this;
    }

    public Command setCancel(Runnable r) {
        this.end = (interrupted) -> {
            if (interrupted)
                r.run();
        };
        return this;
    }

    public Command onInterrupt(Runnable r) {
        return setCancel(r);
    }

    public Command setDone(BooleanSupplier r) {
        this.done = r;
        return this;
    }

    public Command setInterruptibility(Interruptibility i) {
        this.interruptibility = i;
        return this;
    }

    public Command setRequirements(Object... requirements) {
        this.requirements = Arrays.asList(requirements);
        return this;
    }

    public Command setChainability(Chainability c) {
        this.chainability = c;
        return this;
    }

    public Chainability getChainability() {
        return chainability;
    }

    public List<Object> getRequirements() {
        return requirements;
    }

    public Interruptibility getInterruptibility() {
        return interruptibility;
    }

    public Command copy() {
        Command copy = new Command()
                .setExecute(this.execute)
                .setStart(this.start)
                .setEnd(this.end)
                .setDone(this.done)
                .setInterruptibility(this.interruptibility);
        if (this.requirements != null) {
            copy.setRequirements(this.requirements.toArray());
        }
        copy.setChainability(this.chainability);
        return copy;
    }
}
