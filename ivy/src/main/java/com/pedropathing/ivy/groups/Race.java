package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;

import java.util.*;

public class Race implements ICommand {
    private LinkedList<ICommand> commands = new LinkedList<>();
    private List<Object> requirements = new ArrayList<>();
    protected boolean raceCompleted = false;

    public Race(ICommand... cmds) {
        commands.addAll(Arrays.asList(cmds));
        rebuildRequirements();
    }

    @Override
    public void execute() {
        if (!done()) {
            Iterator<ICommand> it = commands.iterator();
            while (it.hasNext()) {
                ICommand command = it.next();
                if (command.done()) {
                    raceCompleted = true;
                    break;
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
    public void end(boolean interrupted) {
        for (ICommand command : commands) {
            command.end(interrupted);
        }
        commands.clear();
    }

    @Override
    public ICommand copy() {
        ICommand[] cmds = new ICommand[commands.size()];
        int i = 0;
        for (ICommand command : commands) {
            cmds[i++] = command.copy();
        }
        return new Race(cmds);
    }

    @Override
    public void start() {
        for (ICommand command : commands) {
            command.start();
        }
    }

    private void rebuildRequirements() {
        Set<Object> set = new HashSet<>();
        for (ICommand command : commands) {
            List<Object> r = command.getRequirements();
            if (r != null) set.addAll(r);
        }
        requirements = new ArrayList<>(set);
    }

    @Override
    public boolean done() {
        return raceCompleted;
    }
}
