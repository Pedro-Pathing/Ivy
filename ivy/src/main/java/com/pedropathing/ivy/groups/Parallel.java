package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;

import java.util.*;

public class Parallel implements ICommand {
    private LinkedList<ICommand> commands = new LinkedList<>();
    private List<Object> requirements;

    public Parallel(ICommand... cmds) {
        commands.addAll(Arrays.asList(cmds));

        HashSet<Object> commandSet = new HashSet<>();
        for (ICommand command : commands) {
            commandSet.addAll(command.getRequirements());
        }
        requirements = new ArrayList<>(commandSet);
    }

    @Override
    public void execute() {
        if (!done()) {
            Iterator<ICommand> it = commands.iterator();
            while (it.hasNext()) {
                ICommand command = it.next();
                if (command.done()) {
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
        return Interruptibility.UNINTERRUPTIBLE;
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public ICommand copy() {
        ICommand[] cmds = new ICommand[commands.size()];
        int i = 0;
        for (ICommand command : commands) {
            cmds[i++] = command.copy();
        }
        return new Parallel(cmds);
    }

    @Override
    public void start() {}

    @Override
    public boolean done() {
        return commands.isEmpty();
    }
}
