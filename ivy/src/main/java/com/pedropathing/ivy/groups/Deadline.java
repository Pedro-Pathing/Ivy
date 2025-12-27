package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;
import com.pedropathing.ivy.Chainability;

import java.util.*;

/**
 * A command group that runs multiple commands in parallel, with one command
 * designated as the
 * "deadline" command. The group ends when the deadline command finishes, at
 * which point all other
 * commands are interrupted.
 *
 * @version 1.0
 * @author Kabir Goyal
 */
public class Deadline implements ICommand {
    private LinkedList<ICommand> commands = new LinkedList<>();
    private ICommand deadlineCommand;
    private List<Object> requirements = new ArrayList<>();
    private boolean done;
    Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;
    private Chainability chainability = Chainability.UNCHAINABLE;

    /**
     * Constructs a new Deadline command group with the passed in commands, where
     * the first command is the deadline command.
     *
     * @param cmds the commands to run in parallel, with the first command as the
     *             deadline
     */
    public Deadline(ICommand... cmds) {
        deadlineCommand = cmds[0];
        commands.addAll(Arrays.asList(cmds).subList(1, cmds.length));
        rebuildRequirements();
        generateInterruptibility();
    }

    /**
     * Runs all commands in parallel until the deadline command completes, then
     * ends the others.
     * Not to be called by the user directly, use a scheduler instead.
     */
    public void execute() {
        if (!done()) {
            if (deadlineCommand.done()) {
                done = true;
                deadlineCommand.end(false);
                for (ICommand command : commands) {
                    command.end(true);
                }
                commands.clear();
                return;
            }
            deadlineCommand.execute();

            Iterator<ICommand> it = commands.iterator();
            while (it.hasNext()) {
                ICommand command = it.next();
                if (command.done()) {
                    command.end(false);
                    it.remove();
                } else {
                    command.execute();
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
        if (deadlineCommand.getInterruptibility() == Interruptibility.UNINTERRUPTIBLE) {
            interruptibility = Interruptibility.UNINTERRUPTIBLE;
            return;
        }

        for (ICommand command : commands) {
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
        for (ICommand command : commands) {
            command.end(interrupted);
        }

        if (!done) {
            deadlineCommand.end(interrupted);
        }

        commands.clear();
        done = true;
    }

    /**
     * @return a copy of this command group
     */
    public ICommand copy() {
        ICommand[] cmds = new ICommand[commands.size()];
        int i = 0;
        for (ICommand command : commands) {
            cmds[i++] = command.copy();
        }

        ICommand[] all = new ICommand[cmds.length + 1];
        all[0] = deadlineCommand.copy();
        System.arraycopy(cmds, 0, all, 1, cmds.length);
        return new Deadline(all).setChainability(chainability);
    }

    /**
     * Starts all commands in this command group
     * Not to be called by the user directly, use a scheduler instead.
     */
    public void start() {
        deadlineCommand.start();
        for (ICommand command : commands) {
            command.start();
        }
    }

    /**
     * @return whether the deadline command has completed
     *         Not to be called by the user directly, use a scheduler instead.
     */
    public boolean done() {
        return done;
    }

    /**
     * Rebuilds the requirements list for this command group
     */
    protected void rebuildRequirements() {
        Set<Object> set = new HashSet<>();
        List<Object> deadlineRequirements = deadlineCommand.getRequirements();
        if (deadlineRequirements != null)
            set.addAll(deadlineRequirements);
        for (ICommand command : commands) {
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
    public Deadline setChainability(Chainability chainability) {
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
