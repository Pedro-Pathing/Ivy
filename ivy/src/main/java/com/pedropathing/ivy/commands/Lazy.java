package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

import java.util.function.Supplier;

/**
 * A command that defers the creation of its behavior until it is started.
 *
 * @version 1.0
 * @author Havish Sripada
 * @author Kabir Goyal
 */
class Lazy extends CommandBuilder {
    private final Supplier<Command> commandSupplier;
    private Command command;

    /**
     * Constructs a new Lazy command that uses the given supplier to create its
     * behavior when started.
     *
     * @param commandSupplier the supplier that provides the command to run when
     *                        started
     */
    public Lazy(Supplier<Command> commandSupplier) {
        this.commandSupplier = commandSupplier;

        setStart(() -> {
            command = commandSupplier.get();
            if (command != null) {
                command.start();
            }
        });

        setExecute(() -> {
            if (done()) return;
            if (command != null) {
                command.execute();
            }
        });

        setDone(() -> command == null || command.done());

        setEnd(endCondition -> {
            if (command != null) {
                command.end(endCondition);
            }
        });
    }
}
