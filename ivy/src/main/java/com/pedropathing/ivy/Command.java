package com.pedropathing.ivy;

import com.pedropathing.ivy.groups.Parallel;
import com.pedropathing.ivy.groups.Sequential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Command implements ICommand {
    private List<Object> requirements = new ArrayList<>();
    private Runnable execute, start, end;
    private BooleanSupplier done;
    private Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;

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
            end.run();
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

    public Command setEnd(Runnable r) {
        this.end = r;
        return this;
    }

    public Command setDone(BooleanSupplier r) {
        this.done = r;
        return this;
    }

    public Command setInterruptibility(Interruptibility i) {
        this.interruptibility = i;
        return this;
    }

    public Command setRequirement(Object... requirements) {
        this.requirements = Arrays.asList(requirements);
        return this;
    }

    public Sequential then(ICommand then) {
        return new Sequential(this, then);
    }

    public Parallel with(ICommand with) {
        return new Parallel(this, with);
    }

    public List<Object> getRequirements() { return requirements; }
    public Interruptibility getInterruptibility() { return interruptibility; }

    public Command copy() {
        Command copy = new Command()
                .setExecute(this.execute)
                .setStart(this.start)
                .setEnd(this.end)
                .setDone(this.done)
                .setInterruptibility(this.interruptibility);
        if (this.requirements != null) {
            copy.setRequirement(this.requirements.toArray());
        }
        return copy;
    }
}