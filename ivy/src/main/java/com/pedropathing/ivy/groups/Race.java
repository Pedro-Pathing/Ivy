package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A command group that runs multiple commands in parallel and completes when
 * the first command finishes. The remaining commands are then interrupted.
 *
 * @author Baron Henderson
 * @author Kabir Goyal
 * @version 1.0
 */
public class Race extends CommandBuilder {
    private final Map<Command, Boolean> commands = new HashMap<>();
    private boolean raceCompleted = false;

    /**
     * Constructs a new Race group with the passed in commands
     *
     * @param children the commands to run in parallel
     */
    public Race(Command... children) {
        Arrays.stream(children).forEach(command -> commands.put(command, false));

        requiring(
                Arrays.stream(children)
                        .flatMap(command -> command.requirements().stream())
                        .collect(Collectors.toSet())
        );

        setPriority(Arrays.stream(children).mapToInt(Command::priority).max().orElse(0));

        setExecute(() -> {
            if (done()) return;

            for (Command command : commands.keySet()) {
                if (command.done()) {
                    raceCompleted = true;
                    command.end(EndCondition.NATURALLY);
                    commands.put(command, true);
                    break;
                }
                command.execute();
            }

            if (raceCompleted) {
                commands.entrySet().stream()
                        .filter(entry -> !entry.getValue())
                        .forEach(entry -> entry.getKey().end(EndCondition.INTERRUPTED));
            }
        });

        setEnd(endCondition -> {
            if (raceCompleted) return;
            commands.entrySet().stream()
                    .filter(entry -> !entry.getValue())
                    .forEach(entry -> entry.getKey().end(endCondition));
        });

        setStart(() -> {
            raceCompleted = false;
            commands.keySet().forEach(command -> {
                commands.put(command, false);
                command.start();
            });
        });

        setDone(() -> raceCompleted || commands.isEmpty());
    }
}
