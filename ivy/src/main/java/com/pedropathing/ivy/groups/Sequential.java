package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;
import com.pedropathing.ivy.Chainability;

import java.util.*;

/**
 * A command group that runs commands in sequence, one after the other.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public class Sequential implements ICommand {
    protected ArrayDeque<ICommand> commands = new ArrayDeque<>();
    protected List<Object> requirements = new ArrayList<>();
    private Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;
    private Chainability chainability = Chainability.UNCHAINABLE;

    /**
     * Constructs a new Sequential command group with the passed in commands
     * 
     * @param cmds the commands to run in sequence, in order
     */
    public Sequential(ICommand... cmds) {
        commands.addAll(Arrays.asList(cmds));
        rebuildRequirements();
        generateInterruptibility();
    }

    /**
     * Constructs an empty Sequential command group
     */
    public Sequential() {
    }

    /**
     * Runs the current command in the sequence. If the current command completes,
     * it ends it and starts the next command.
     * Not to be called by the user directly, use a scheduler instead.
     */
    public void execute() {
        if (done())
            return;

        ICommand current = commands.peek();
        assert current != null;

        if (current.done()) {
            current.end(false);
            commands.poll();
            if (!done()) {
                ICommand next = commands.peek();
                if (next != null)
                    next.start();
            }
        } else {
            current.execute();
        }
    }

    /**
     * @return the list of requirements for this command group, the union of the
     *         requirements of its subcommands
     */
    public List<Object> getRequirements() {
        return requirements;
    }

    /**
     * @return the interruptibility of this command group, UNINTERRUPTIBLE if any
     *         subcommand is UNINTERRUPTIBLE, otherwise INTERRUPTIBLE
     */
    public Interruptibility getInterruptibility() {
        return interruptibility;
    }

    /**
     * sets the interruptibility of this command group based on its subcommands
     */
    protected void generateInterruptibility() {
        for (ICommand command : commands) {
            if (command.getInterruptibility() == Interruptibility.UNINTERRUPTIBLE) {
                interruptibility = Interruptibility.UNINTERRUPTIBLE;
                return;
            }
        }
    }

    /**
     * Ends all commands in the sequence, in order
     * Not to be called by the user directly, use a scheduler instead.
     * 
     * @param interrupted whether the command group was interrupted or ended
     *                    normally
     */
    public void end(boolean interrupted) {
        while (!commands.isEmpty()) {
            ICommand c = commands.poll();
            if (c != null) {
                c.end(interrupted);
            }
        }
    }

    /**
     * @return a copy of this Sequential command group with copies of all its
     *         subcommands
     */
    public ICommand copy() {
        ICommand[] cmds = new ICommand[commands.size()];
        int i = 0;
        for (ICommand command : commands) {
            cmds[i++] = command.copy();
        }
        return new Sequential(cmds).setChainability(chainability);
    }

    /**
     * Starts the first command in the sequence
     * Not to be called by the user directly, use a scheduler instead.
     */
    public void start() {
        if (!done()) {
            ICommand current = commands.peek();
            if (current != null)
                current.start();
        }
    }

    /**
     * @return whether all commands in the sequence have completed
     *         Not to be called by the user directly, use a scheduler instead.
     */
    public boolean done() {
        return commands.isEmpty();
    }

    /**
     * Creates the requirements for this command group, the union of the
     * requirements of its subcommands
     */
    protected void rebuildRequirements() {
        Set<Object> set = new HashSet<>();
        for (ICommand command : commands) {
            List<Object> r = command.getRequirements();
            if (r != null)
                set.addAll(r);
        }
        requirements = new ArrayList<>(set);
    }

    /**
     * @return the chainability of this command group
     */
    public Chainability getChainability() {
        return chainability;
    }

    /**
     * Sets the chainability of this command group
     * 
     * @param chainability the chainability to set
     * @return this, so setters can be chained
     */
    public Sequential setChainability(Chainability chainability) {
        this.chainability = chainability;
        return this;
    }
}
