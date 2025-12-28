package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Chainability;
import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;

import java.util.*;

/**
 * A command group that runs multiple commands in parallel and completes when
 * the first command finishes. The remaining commands are then interrupted.
 * 
 * @version 1.0
 * @author Kabir Goyal
 */
public class Race implements ICommand {
    private LinkedList<ICommand> commands = new LinkedList<>();
    private List<Object> requirements = new ArrayList<>();
    protected boolean raceCompleted = false;
    private Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;
    private Chainability chainability = Chainability.UNCHAINABLE;

    /**
     * Constructs a new Race group with the passed in commands
     */
    public Race(ICommand... cmds) {
        commands.addAll(Arrays.asList(cmds));
        rebuildRequirements();
        generateInterruptibility();
    }

    /**
     * Runs all commands in parallel until one completes, then ends the others.
     * Not to be called by the user directly, use a scheduler instead.
     */
    public void execute() {
        if (!done()) {
            Iterator<ICommand> it = commands.iterator();
            ICommand winner = null;
            while (it.hasNext()) {
                ICommand command = it.next();
                if (command.done()) {
                    raceCompleted = true;
                    command.end(false);
                    winner = command;
                    break;
                } else {
                    command.execute();
                }
            }

            if (raceCompleted) {
                for (ICommand cmd : commands) {
                    if (cmd != winner) {
                        cmd.end(true);
                    }
                }
            }
        }
    }

    /**
     * @return the list of requirements for this command group
     */
    public List<Object> getRequirements() {
        return requirements;
    }

    /**
     * @return the interruptibility of this command group
     */
    public Interruptibility getInterruptibility() {
        return interruptibility;
    }

    /**
     * Sets the interruptibility of this command group based on its subcommands
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
     * Ends all commands in this group
     * 
     * @param interrupted whether the commands were interrupted
     *                    Not to be called by the user directly, use a scheduler
     *                    instead.
     */
    public void end(boolean interrupted) {
        for (ICommand command : commands) {
            command.end(interrupted);
        }
    }

    /**
     * @return a copy of this Race command group with copies of all its subcommands
     */
    public ICommand copy() {
        ICommand[] cmds = new ICommand[commands.size()];
        int i = 0;
        for (ICommand command : commands) {
            cmds[i++] = command.copy();
        }
        return new Race(cmds).setChainability(chainability);
    }

    /**
     * Starts all commands in this group
     * Not to be called by the user directly, use a scheduler instead.
     */
    public void start() {
        raceCompleted = false;
        for (ICommand command : commands) {
            command.start();
        }
    }

    /**
     * Rebuilds the requirements list based on the current subcommands
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
     * @return whether any command in the group has completed
     *         Not to be called by the user directly, use a scheduler instead.
     */
    public boolean done() {
        return raceCompleted;
    }

    /**
     * @return the chainability of this command group
     *         Not to be called by the user directly, use a scheduler instead.
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
    public Race setChainability(Chainability chainability) {
        this.chainability = chainability;
        return this;
    }
}
