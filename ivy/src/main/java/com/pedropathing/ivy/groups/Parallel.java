package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Chainability;
import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;

import java.util.*;

/**
 * A command group that runs multiple commands in parallel.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public class Parallel implements ICommand {
    protected HashMap<ICommand, Boolean> commands = new HashMap<>();
    private List<Object> requirements = new ArrayList<>();
    private Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;
    private Chainability chainability = Chainability.UNCHAINABLE;
    private int numCompleted = 0;

    /**
     * Constructs a new Parallel command group with the passed in commands
     * 
     * @param cmds the commands to run in parallel
     */
    public Parallel(ICommand... cmds) {
        for (ICommand cmd : cmds) {
            commands.put(cmd, false);
        }
        rebuildRequirements();
        generateInterruptibility();
    }

    /**
     * Runs all commands in parallel. Not to be called by the user directly, use a
     * scheduler instead.
     */
    public void execute() {
        if (!done()) {
            for (ICommand command : commands.keySet()) {
                if (!commands.get(command)) {
                    if (command.done()) {
                        command.end(false);
                        numCompleted++;
                        commands.put(command, true);
                    } else {
                        command.execute();
                    }
                }
            }
        }
    }

    /**
     * @return returns the list of requirements for this command group
     */
    public List<Object> getRequirements() {
        return requirements;
    }

    /**
     * @return returns the interruptibility of this command group
     */
    @Override
    public Interruptibility getInterruptibility() {
        return interruptibility;
    }

    /**
     * Generates the interruptibility of this command group based on its subcommands
     */
    protected void generateInterruptibility() {
        for (ICommand command : commands.keySet()) {
            if (command.getInterruptibility() == Interruptibility.UNINTERRUPTIBLE) {
                interruptibility = Interruptibility.UNINTERRUPTIBLE;
                return;
            }
        }
    }

    /**
     * Ends all commands in this command group
     * Not to be called by the user directly, use a scheduler instead.
     * 
     * @param interrupted whether the command was interrupted or ended normally
     */
    public void end(boolean interrupted) {
        for (ICommand command : commands.keySet()) {
            if (!commands.get(command)) {
                command.end(interrupted);
            }
        }
    }

    /**
     * @return a copy of this command group
     */
    public ICommand copy() {
        ICommand[] cmds = new ICommand[commands.size()];
        int i = 0;
        for (ICommand command : commands.keySet()) {
            cmds[i++] = command.copy();
        }
        return new Parallel(cmds).setChainability(this.chainability);
    }

    /**
     * Starts all commands in this command group
     * Not to be called by the user directly, use a scheduler instead.
     */
    public void start() {
        numCompleted = 0;
        for (ICommand command : commands.keySet()) {
            commands.put(command, false);
            command.start();
        }
    }

    /**
     * Not to be called by the user directly, use a scheduler instead.
     * 
     * @return returns whether all commands in this command group have finished
     *         executing
     */
    public boolean done() {
        return numCompleted >= commands.size();
    }

    /**
     * Sets the requirements list for this command group, the union of the
     * requirements of its subcommands
     */
    protected void rebuildRequirements() {
        Set<Object> set = new HashSet<>();
        for (ICommand command : commands.keySet()) {
            List<Object> r = command.getRequirements();
            if (r != null)
                set.addAll(r);
        }
        requirements = new ArrayList<>(set);
    }

    /**
     * Sets the chainability of this command group
     * 
     * @param chainability the chainability to set
     * @return this command group
     */
    public Parallel setChainability(Chainability chainability) {
        this.chainability = chainability;
        return this;
    }

    /**
     * @return the chainability of this command group
     */
    public Chainability getChainability() {
        return chainability;
    }
}
