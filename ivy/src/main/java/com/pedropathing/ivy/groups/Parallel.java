package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A command group that runs multiple commands in parallel.
 *
 * @author Baron Henderson
 * @author Kabir Goyal
 * @version 1.0
 */
public class Parallel extends CommandBuilder {
    private final Map<Command, Boolean> commands = new HashMap<>();

    /**
     * Constructs a new Parallel command group with the passed in commands
     *
     * @param children the commands to run in parallel
     */
    public Parallel(Command... children) {
        Arrays.stream(children).forEach(command -> commands.put(command, false));

        requiring(
                Arrays.stream(children)
                        .flatMap(command -> command.requirements().stream())
                        .collect(Collectors.toSet())
        );

        setPriority(Arrays.stream(children).mapToInt(Command::priority).max().orElse(0));

        setExecute(() -> {
            if (done()) return;

            commands.keySet().forEach(command -> {
                if (commands.get(command)) return;

                if (command.done()) {
                    command.end(EndCondition.NATURALLY);
                    commands.put(command, true);
                    return;
                }

                command.execute();
            });
        });

        setEnd(endCondition -> {
            commands.entrySet().stream()
                    .filter(entry -> !entry.getValue())
                    .forEach(command -> {
                        command.getKey().end(endCondition);
                    });
        });

        setStart(() -> {
            commands.forEach((command, done) -> {
                commands.put(command, false);
                command.start();
            });
        });

        setDone(() -> commands.values().stream().filter(done -> done).count() >= commands.size());
    }
}
