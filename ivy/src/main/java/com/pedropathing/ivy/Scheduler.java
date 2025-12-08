package com.pedropathing.ivy;

import java.util.*;

public class Scheduler {
    private static final Scheduler instance = new Scheduler();
    private LinkedList<ICommand> commands = new LinkedList<>();
//    private HashMap<Object, ICommand> commandMap = new HashMap<>();

    public static Scheduler getInstance() {
        return instance;
    }

//    private void remove(ICommand cmd) {
//        commands.remove(cmd);
//    }

    public void schedule(ICommand... cmds) {
        //            List<Object> requirements = cmd.getRequirements();
        //            Object conflictObject = null;
        //            for (Object req : requirements) {
        //                if (commandMap.containsKey(req)) {
        //                    conflictObject = req;
        //                    break;
        //                }
        //            }
        //            if (conflictObject != null) {
        //                Interruptibility interruptibility = commandMap.get(conflictObject).getInterruptibility();
        //
        //                if (interruptibility == Interruptibility.INTERRUPTIBLE) {
        //                    commandMap.get(conflictObject).end(true);
        //                    remove(commandMap.get(conflictObject));
        //                } else {
        //                    continue;
        //                }
        //            }
        //            for (Object req : cmd.getRequirements()) {
        //                commandMap.put(req, cmd);
        //            }
        commands.addAll(Arrays.asList(cmds));
    }

    public void execute() {
        if (!commands.isEmpty()) {
            Iterator<ICommand> it = commands.iterator();
            while (it.hasNext()) {
                ICommand command = it.next();
                if (command.done()) {
                    it.remove();
                    if (!commands.isEmpty())
                        commands.getFirst().start();
                } else {
                    command.execute();
                }
            }
        }
    }

    public void reset() {
        commands.clear();
    }

}
