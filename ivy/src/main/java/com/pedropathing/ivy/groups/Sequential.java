package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A command group that runs commands in sequence, one after the other.
 *
 * @author Baron Henderson
 * @author Kabir Goyal
 * @version 1.0
 */
public class Sequential extends CommandBuilder {
    private final List<Command> commands;
    private int index = 0;

    /**
     * Constructs a new Sequential command group with the passed in commands
     *
     * @param children the commands to run in sequence, in order
     */
    public Sequential(Command... children) {
        commands = Arrays.asList(children);

        requiring(
                commands.stream()
                        .flatMap(command -> command.requirements().stream())
                        .collect(Collectors.toSet())
        );

        setPriority(commands.stream().mapToInt(Command::priority).max().getAsInt());

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
            if (!done()) {
                Command current = commands.get(index);
                if (current != null) current.start();
            }
        });

        setDone(() -> index >= commands.size());
    }
}
