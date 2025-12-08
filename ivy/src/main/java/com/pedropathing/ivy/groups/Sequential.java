package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;

import java.util.*;

public class Sequential implements ICommand {
    protected ArrayDeque<ICommand> commands = new ArrayDeque<>();
    protected List<Object> requirements;

    public Sequential(ICommand... cmds) {
        commands.addAll(Arrays.asList(cmds));

        HashSet<Object> commandSet = new HashSet<>();
        for (ICommand command : commands) {
            commandSet.addAll(command.getRequirements());
        }
        requirements = new ArrayList<>(commandSet);
    }

    public Sequential() {
    }

    @Override
    public void execute() {
        if (!done()) {
            ICommand current = commands.peek();

            assert current != null;

            if (current.done())
                commands.poll();
            else
                current.execute();

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
        return new Sequential(cmds);
    }

    @Override
    public void start() {}

    @Override
    public boolean done() {
        return commands.isEmpty();
    }
}
