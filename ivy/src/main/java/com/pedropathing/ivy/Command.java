package com.pedropathing.ivy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Default implementation of ICommand. Behavior can be set via setters.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public class Command implements ICommand {
    public static final Command NOOP = new Command();

    private List<Object> requirements = new ArrayList<>();
    private Runnable execute, start;
    private Consumer<Boolean> end;
    private BooleanSupplier done;
    private Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;
    private Chainability chainability = Chainability.UNCHAINABLE;

    /**
     * Executes the command's periodic behavior.
     * This should not be called directly -- use a scheduler to run commands.
     */
    public void execute() {
        if (execute != null) {
            execute.run();
        }
    }

    /**
     * executes the command's starting behavior.
     * This should not be called directly -- use a scheduler to run commands.
     */
    public void start() {
        if (start != null) {
            start.run();
        }
    }

    /**
     * executes the command's ending behavior.
     * 
     * @param interrupted whether the command was interrupted or ended normally
     *                    This should not be called directly -- use a scheduler to
     *                    run commands.
     */
    public void end(boolean interrupted) {
        if (end != null) {
            end.accept(interrupted);
        }
    }

    /**
     * @return whether the command has finished executing
     *         This should not be called directly -- use a scheduler to run
     *         commands.
     */
    public boolean done() {
        if (done != null) {
            return done.getAsBoolean();
        }
        return false;
    }

    /**
     * Sets the command's periodic behavior.
     * 
     * @param r the behavior to set, as a Runnable (void method with no parameters)
     * @return this, so setters can be chained
     */
    public Command setExecute(Runnable r) {
        this.execute = r;
        return this;
    }

    /**
     * Sets the command's starting behavior.
     * 
     * @param r the behavior to set, as a Runnable (void method with no parameters)
     * @return this, so setters can be chained
     */
    public Command setStart(Runnable r) {
        this.start = r;
        return this;
    }

    /**
     * Sets the command's ending behavior.
     * 
     * @param c the behavior to set, as a Consumer<Boolean> (void method with a
     *          boolean parameter)
     * @return this, so setters can be chained
     */
    public Command setEnd(Consumer<Boolean> c) {
        this.end = c;
        return this;
    }

    /**
     * Sets the command's cancel behavior.
     * This is equivalent to calling setEnd with a Consumer that only runs when
     * interrupted is true.
     * 
     * @param r the behavior to set, as a Runnable (void method with no parameters)
     * @return this, so setters can be chained
     */
    public Command setCancel(Runnable r) {
        this.end = (interrupted) -> {
            if (interrupted)
                r.run();
        };
        return this;
    }

    /**
     * Sets the command's behavior on interrupt.
     * This is equivalent to calling setEnd with a Consumer that only runs when
     * interrupted is true.
     * 
     * @param r the behavior to set, as a Runnable (void method with no parameters)
     * @return this, so setters can be chained
     */
    public Command onInterrupt(Runnable r) {
        return setCancel(r);
    }

    /**
     * Sets the command's completion condition.
     * 
     * @param b the condition to set, as a BooleanSupplier (boolean method with no
     *          parameters)
     * @return this, so setters can be chained
     */
    public Command setDone(BooleanSupplier b) {
        this.done = b;
        return this;
    }

    /**
     * Sets the command's interruptibility.
     * 
     * @param i the interruptibility to set
     * @return this, so setters can be chained
     */
    public Command setInterruptibility(Interruptibility i) {
        this.interruptibility = i;
        return this;
    }

    /**
     * Sets the command's requirements.
     * 
     * @param requirements the requirements to set
     * @return this, so setters can be chained
     */
    public Command setRequirements(Object... requirements) {
        this.requirements = Arrays.asList(requirements);
        return this;
    }

    /**
     * Sets the command's chainability.
     * 
     * @param c the chainability to set
     * @return this, so setters can be chained
     */
    public Command setChainability(Chainability c) {
        this.chainability = c;
        return this;
    }

    /**
     * @return the command's chainability
     */
    public Chainability getChainability() {
        return chainability;
    }

    /**
     * @return the command's requirements
     */
    public List<Object> getRequirements() {
        return requirements;
    }

    /**
     * @return the command's interruptibility
     */
    public Interruptibility getInterruptibility() {
        return interruptibility;
    }

    /**
     * @return a copy of this command
     */
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

    /**
     * Adopts the behavior of another ICommand.
     * Don't use this method directly; it's intended for use in other Ivy Classes.
     * 
     * @param v the ICommand to adopt behavior from
     */
    public void adoptBehavior(ICommand v) {
        v.start();
        setExecute(v::execute);
        setEnd(v::end);
        setDone(v::done);
        setInterruptibility(v.getInterruptibility());
        setChainability(v.getChainability());
        if (v.getRequirements() != null)
            setRequirements(v.getRequirements().toArray());
    }

    /**
     * Adopts the behavior of another ICommand.
     * Don't use this method directly; it's intended for use in other Ivy Classes.
     * 
     * @param v the ICommand to adopt behavior from
     */
    public void adoptBehaviorWithoutStarting(ICommand v) {
        setStart(v::start);
        setExecute(v::execute);
        setEnd(v::end);
        setDone(v::done);
        setInterruptibility(v.getInterruptibility());
        setChainability(v.getChainability());
        if (v.getRequirements() != null)
            setRequirements(v.getRequirements().toArray());
    }
}
