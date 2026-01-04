package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A command group that runs multiple commands in parallel, with one command
 * designated as the
 * "deadline" command. The group ends when the deadline command finishes, at
 * which point all other
 * commands are interrupted.
 *
 * @author Baron Henderson
 * @author Kabir Goyal
 * @version 1.0
 */
class Deadline extends CommandBuilder {
    private final Map<Command, Boolean> commands = new HashMap<>();
    private boolean deadlineCompleted = false;

    /**
     * Constructs a new Deadline command group with the specified deadline command
     * and additional commands to run in parallel. The group ends when the deadline
     * command finishes, at which point all other commands are interrupted.
     *
     * @param deadline the command that determines when the group ends
     * @param children the other commands to run in parallel with the deadline
     */
    public Deadline(Command deadline, Command... children) {
        Arrays.stream(children).forEach(command -> commands.put(command, false));

        requiring(
                Arrays.stream(children)
                        .flatMap(command -> command.requirements().stream())
                        .collect(Collectors.toSet())
        );
        requirements().addAll(deadline.requirements());

        setPriority(Math.max(
                deadline.priority(),
                Arrays.stream(children).mapToInt(Command::priority).max().orElse(0)
        ));

        setExecute(() -> {
            if (done()) return;

            if (deadline.done()) {
                deadlineCompleted = true;
                deadline.end(EndCondition.NATURALLY);
                commands.entrySet().stream()
                        .filter(entry -> !entry.getValue())
                        .forEach(entry -> entry.getKey().end(EndCondition.INTERRUPTED));
                return;
            }
            deadline.execute();

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
            if (!deadlineCompleted) {
                deadline.end(endCondition);
            }
            commands.entrySet().stream()
                    .filter(entry -> !entry.getValue())
                    .forEach(entry -> entry.getKey().end(endCondition));
        });

        setStart(() -> {
            deadlineCompleted = false;
            deadline.start();
            commands.keySet().forEach(command -> {
                commands.put(command, false);
                command.start();
            });
        });

        setDone(() -> deadlineCompleted);
    }
}
