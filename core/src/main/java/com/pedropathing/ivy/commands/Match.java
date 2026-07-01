package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;

import java.util.EnumMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A command that selects and runs one of several commands based on the value
 * of an enum state.
 *
 * @author Kabir Goyal
 * @version 1.0
 */
class Match<T extends Enum<T>> extends CommandBuilder {
    private final Supplier<T> stateSupplier;
    private final EnumMap<T, Command> cases;
    private Command selected;

    /**
     * Constructs a new Match command that selects a command to run based on
     * the value provided by the state supplier.
     *
     * @param stateSupplier a supplier (function with no parameters) that
     *                      provides the current enum state
     * @param cases         an EnumMap mapping enum branches to commands
     */
    public Match(Supplier<T> stateSupplier, EnumMap<T, Command> cases) {
        this.stateSupplier = stateSupplier;
        this.cases = cases;

        requiring(
                cases.values().stream()
                        .flatMap(command -> command.requirements().stream())
                        .collect(Collectors.toSet())
        );

        setPriority(cases.values().stream().mapToInt(Command::priority).max().orElse(0));

        setStart(() -> {
            T state = stateSupplier.get();
            selected = cases.get(state);
            if (selected != null) {
                selected.start();
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
