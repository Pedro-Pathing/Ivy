package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;

import java.util.*;

public class Sequential implements ICommand {
    protected ArrayDeque<ICommand> commands = new ArrayDeque<>();
    protected List<Object> requirements = new ArrayList<>();

    public Sequential(ICommand... cmds) {
        commands.addAll(Arrays.asList(cmds));

        rebuildRequirements();
    }

    public Sequential() {
    }

    @Override
    public void execute() {
        if (done()) return;

        ICommand current = commands.peek();
        assert current != null;

        if (current.done()) {
            current.end(false);
            commands.poll();
            if (!done()) {
                ICommand next = commands.peek();
                if (next != null) next.start();
            }
        } else {
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
    public void end(boolean interrupted) {
        while (!commands.isEmpty()) {
            ICommand c = commands.poll();
            if (c != null) {
                c.end(interrupted);
            }
        }
    }

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
    public void start() {
        if (!done()) {
            ICommand current = commands.peek();
            if (current != null) current.start();
        }
    }

    @Override
    public boolean done() {
        return commands.isEmpty();
    }

    private void rebuildRequirements() {
        Set<Object> set = new HashSet<>();
        for (ICommand command : commands) {
            List<Object> r = command.getRequirements();
            if (r != null) set.addAll(r);
        }
        requirements = new ArrayList<>(set);
    }
}
