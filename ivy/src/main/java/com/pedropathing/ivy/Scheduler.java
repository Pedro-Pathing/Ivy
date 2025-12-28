package com.pedropathing.ivy;

import com.pedropathing.ivy.commands.Instant;
import com.pedropathing.ivy.commands.WaitUntil;
import com.pedropathing.ivy.groups.Sequential;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The Scheduler is responsible for managing and executing commands.
 * It ensures that commands are scheduled according to their requirements,
 * interruptibility, and chainability.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public class Scheduler {
    private static final Scheduler instance = new Scheduler();
    private final LinkedList<ICommand> commands = new LinkedList<>();
    private final HashMap<Object, ICommand> commandMap = new HashMap<>();

    /**
     * Gets the singleton instance of the Scheduler
     * 
     * @return
     */
    public static Scheduler getInstance() {
        return instance;
    }

    /**
     * Removes a command from the scheduler
     * 
     * @param cmd command to be removed
     */
    private void remove(ICommand cmd) {
        commands.remove(cmd);
        removeRequirements(cmd);
    }

    /**
     * Removes the requirements of a command from the command map
     * 
     * @param cmd command whose requirements are to be removed
     */
    private void removeRequirements(ICommand cmd) {
        for (Object req : cmd.getRequirements()) {
            commandMap.remove(req);
        }
    }

    /**
     * Schedules one or more commands for execution <b>in parallel</b>
     * 
     * @param cmds commands to be scheduled
     */
    public void schedule(ICommand... cmds) {
        for (ICommand cmd : cmds) {
            List<Object> requirements = cmd.getRequirements();
            List<Object> conflictObjects = new ArrayList<>();
            for (Object req : requirements) {
                if (commandMap.containsKey(req)) {
                    conflictObjects.add(req);
                }
            }
            if (!conflictObjects.isEmpty()) {
                Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;
                Set<ICommand> conflictingInterruptibleCommands = new HashSet<>();
                for (Object conflictObject : conflictObjects) {
                    ICommand conflictingCommand = commandMap.get(conflictObject);
                    if (conflictingCommand.getInterruptibility() == Interruptibility.UNINTERRUPTIBLE) {
                        interruptibility = Interruptibility.UNINTERRUPTIBLE;
                        break;
                    } else if (conflictingCommand.getInterruptibility() == Interruptibility.INTERRUPTIBLE) {
                        conflictingInterruptibleCommands.add(conflictingCommand);
                    }
                }
                if (interruptibility == Interruptibility.INTERRUPTIBLE) {
                    for (ICommand conflictingCommand : conflictingInterruptibleCommands) {
                        conflictingCommand.end(true);
                        remove(conflictingCommand);
                    }
                    commands.add(cmd);
                    cmd.start();
                    for (Object req : cmd.getRequirements()) {
                        commandMap.put(req, cmd);
                    }
                } else if (cmd.getChainability() == Chainability.CHAINABLE) {
                    AtomicReference<Sequential> wrapperRef = new AtomicReference<>();

                    Sequential wrapper = new Sequential(
                            new WaitUntil(() -> {
                                for (Object req : requirements) {
                                    if (commandMap.containsKey(req)) {
                                        ICommand conflictingCommand = commandMap.get(req);
                                        if (conflictingCommand
                                                .getInterruptibility() == Interruptibility.UNINTERRUPTIBLE) {
                                            return false;
                                        }
                                    }
                                }
                                return true;
                            }),
                            new Instant(() -> {
                                Set<ICommand> conflictingCommands = new HashSet<>();
                                for (Object conflictObject : conflictObjects) {
                                    ICommand conflictingCommand = commandMap.get(conflictObject);
                                    if (conflictingCommand != null) {
                                        conflictingCommands.add(conflictingCommand);
                                    }
                                }
                                for (ICommand conflictingCommand : conflictingCommands) {
                                    conflictingCommand.end(true);
                                    remove(conflictingCommand);
                                }

                                for (Object req : cmd.getRequirements()) {
                                    commandMap.put(req, wrapperRef.get());
                                }
                            }),
                            cmd);

                    wrapperRef.set(wrapper);
                    commands.add(wrapper);

                }
            } else {
                commands.add(cmd);
                cmd.start();
                for (Object req : cmd.getRequirements()) {
                    commandMap.put(req, cmd);
                }
            }
        }
    }

    /**
     * Executes all scheduled commands <b>in parallel</b>. This method should be
     * called
     * periodically
     */
    public void execute() {
        if (!commands.isEmpty()) {
            List<ICommand> copy = new ArrayList<>(commands);
            Iterator<ICommand> it = copy.iterator();
            while (it.hasNext()) {
                ICommand command = it.next();
                if (command.done()) {
                    command.end(false);
                    removeRequirements(command);
                    it.remove();
                } else {
                    command.execute();
                }
            }
        }
    }

    /**
     * Resets the scheduler by clearing all scheduled commands
     */
    public void reset() {
        commands.clear();
        commandMap.clear();
    }

}
