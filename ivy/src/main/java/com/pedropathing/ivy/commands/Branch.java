package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

/**
 * A command that chooses between multiple commands to run based on boolean
 * conditions.
 * The first command whose condition evaluates to true will be run.
 * If none of the conditions are true, the command finishes immediately.
 *
 * @version 1.0
 * @author Havish Sripada
 * @author Kabir Goyal
 */
public class Branch extends CommandBuilder {
    private final LinkedHashMap<BooleanSupplier, Command> commands;
    private Command selected;

    /**
     * Constructs a new Branch command that runs one of multiple commands based
     * on the results of the given boolean suppliers.
     *
     * @param commands a LinkedHashMap mapping boolean suppliers (boolean
     *                 functions with no parameters) to commands
     *                 The order of insertion determines the priority of the
     *                 commands.
     */
    public Branch(LinkedHashMap<BooleanSupplier, Command> commands) {
        this.commands = commands;

        requiring(
                commands.values().stream()
                        .flatMap(command -> command.requirements().stream())
                        .collect(Collectors.toSet())
        );

        setPriority(commands.values().stream().mapToInt(Command::priority).max().orElse(0));

        setStart(() -> {
            selected = null;
            for (Map.Entry<BooleanSupplier, Command> entry : commands.entrySet()) {
                if (entry.getKey().getAsBoolean()) {
                    selected = entry.getValue();
                    selected.start();
                    return;
                }
            }
        });

        setExecute(() -> {
            if (done()) return;
            if (selected != null) {
                selected.execute();
            }
        });

        setDone(() -> selected == null || selected.done());

        setEnd(endCondition -> {
            if (selected != null) {
                selected.end(endCondition);
            }
        });
    }
}
