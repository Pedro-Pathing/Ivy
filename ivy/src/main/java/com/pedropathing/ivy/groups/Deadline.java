package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;
import com.pedropathing.ivy.Chainability;

import java.util.*;

public class Deadline implements ICommand {
    private LinkedList<ICommand> commands = new LinkedList<>();
    private ICommand deadlineCommand;
    private List<Object> requirements = new ArrayList<>();
    private boolean done;
    Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;
    private Chainability chainability = Chainability.UNCHAINABLE;

    public Deadline(ICommand... cmds) {
        deadlineCommand = cmds[0];
        commands.addAll(Arrays.asList(cmds).subList(1, cmds.length));
        rebuildRequirements();
        generateInterruptibility();
    }

    @Override
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

    @Override
    public List<Object> getRequirements() {
        return requirements;
    }

    @Override
    public Interruptibility getInterruptibility() {
        return interruptibility;
    }

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

    @Override
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

    @Override
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

    @Override
    public void start() {
        deadlineCommand.start();
        for (ICommand command : commands) {
            command.start();
        }
    }

    @Override
    public boolean done() {
        return done;
    }

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

    public Deadline setChainability(Chainability chainability) {
        this.chainability = chainability;
        return this;
    }

    public Chainability getChainability() {
        return chainability;
    }

}
