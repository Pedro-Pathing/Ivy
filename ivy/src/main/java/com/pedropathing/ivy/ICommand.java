package com.pedropathing.ivy;

import com.pedropathing.ivy.commands.Wait;
import com.pedropathing.ivy.groups.Parallel;
import com.pedropathing.ivy.groups.Race;
import com.pedropathing.ivy.groups.Sequential;

import java.util.Collections;
import java.util.List;

/**
 * Command Interface that all types implement
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public interface ICommand {
    /**
     * @return returns list of requirements for the command
     */
    List<Object> getRequirements();

    /**
     * @return returns chainability of the command
     */
    Chainability getChainability();

    /**
     * @return returns interruptibility of the command
     */
    Interruptibility getInterruptibility();

    /**
     * behavior to be executed when the command begins running
     */
    void start();

    /**
     * @return returns if the command has finished executing
     */
    boolean done();

    /**
     * behavior to be executed repeatedly until the command is completed
     */
    void execute();

    /**
     * @param interrupted whether the command was interrupted or ended normally
     *                    behavior to be executed when the command ends
     */
    void end(boolean interrupted);

    /**
     * @return a copy of the command
     */
    ICommand copy();

    /**
     * @param then command to run after this one
     * @return a Sequential group with this command followed by the given command
     */
    default Sequential then(ICommand then) {
        return new Sequential(this, then);
    }

    /**
     * @param with command to run alongside this one
     * @return a Parallel group with this command running alongside the given
     *         command
     */
    default Parallel with(ICommand with) {
        return new Parallel(this, with);
    }

    /**
     * @param milliseconds time in milliseconds before the command times out
     * @return a new Race group with this command racing against a wait command
     */
    default Race timeoutAfter(int milliseconds) {
        return new Race(
                this,
                new Wait(milliseconds));
    }

    public static ICommand noop = new ICommand() {
        @Override
        public List<Object> getRequirements() {
            return Collections.emptyList();
        }

        @Override
        public Chainability getChainability() {
            return Chainability.UNCHAINABLE;
        }

        @Override
        public Interruptibility getInterruptibility() {
            return Interruptibility.INTERRUPTIBLE;
        }

        @Override
        public void start() { }

        @Override
        public boolean done() {
            return true;
        }

        @Override
        public void execute() { }

        @Override
        public void end(boolean interrupted) { }

        @Override
        public ICommand copy() {
            return this;
        }
    };
}
