package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

/**
 * A command group that runs a command multiple times in sequence for a
 * specified number iterations.
 * 
 * @version 1.0
 * @author Kabir Goyal
 */
public class Loop extends CommandBuilder {
    private final IntSupplier iterationsSupplier;
    private final Command command;
    private List<Command> commands;
    private int index = 0;

    /**
     * Constructs a new Loop command group that runs the given command for the
     * specified number of iterations.
     * 
     * @param command    the command to run in a loop
     * @param iterations the number of times to run the command
     */
    public Loop(Command command, int iterations) {
        this(command, () -> iterations);
    }

    /**
     * Constructs a new Loop command group that runs the given command for the
     * specified number of iterations supplied by the given IntSupplier.
     * 
     * @param command            the command to run in a loop
     * @param iterationsSupplier the supplier that provides the number of times to
     *                           run the command
     */
    public Loop(Command command, IntSupplier iterationsSupplier) {
        this.command = command;
        this.iterationsSupplier = iterationsSupplier;

        requiring(command.requirements());

        setPriority(command.priority());

        setExecute(() -> {
            if (done()) return;

            Command current = commands.get(index);

            if (current == null) {
                index++;
                execute();
                return;
            }

            if (current.done()) {
                current.end(EndCondition.NATURALLY);
                index++;
                if (!done()) {
                    Command next = commands.get(index);
                    if (next != null) next.start();
                }
                return;
            }

            current.execute();
        });

        setEnd(endCondition -> {
            if (endCondition == EndCondition.SUSPENDED) {
                commands.get(index).end(endCondition);
            } else {
                for (int i = index; i < commands.size(); i++) {
                    commands.get(i).end(endCondition);
                }
                index = commands.size();
            }
        });

        setStart(() -> {
            index = 0;

            commands =
                java.util.stream.IntStream.range(0, iterationsSupplier.getAsInt())
                .mapToObj(i -> command)
                .collect(Collectors.toList());

            if (!done()) {
                Command current = commands.get(index);
                if (current != null) current.start();
            }
        });

        setDone(() -> index >= commands.size());
    }
}
