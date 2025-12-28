package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.ICommand;

import java.util.function.Supplier;

/**
 * A command that defers the creation of its behavior until it is started.
 *
 * @version 1.0
 * @author Havish Sripada
 * @author Kabir Goyal
 */
public class Lazy extends Command {
    private final Supplier<ICommand> commandSupplier;

    /**
     * Constructs a new Lazy command that uses the given supplier to create its
     * behavior when started.
     *
     * @param commandSupplier the supplier that provides the command to run when
     *                        started
     */
    public Lazy(Supplier<ICommand> commandSupplier) {
        this.commandSupplier = commandSupplier;
    }

    /**
     * Starts the command by adopting the behavior provided by the command
     * supplier.
     * Not to be called directly, use a scheduler instead.
     */
    @Override
    public void start() {
        adoptBehavior(commandSupplier.get());
    }

    /**
     * Creates a copy of this Lazy command.
     *
     * @return a new Lazy command with the same command supplier
     */
    @Override
    public Lazy copy() {
        return new Lazy(commandSupplier);
    }
}
