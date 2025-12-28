package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

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
public class Branch extends CommandClass {
    private final LinkedHashMap<BooleanSupplier, Command> commands;

    /**
     * Constructs a new Optional command that runs one of multiple commands based
     * on the results of the given boolean suppliers.
     * 
     * @param commands a LinkedHashMap mapping boolean suppliers (boolean
     *                 functions with no parameters) to commands
     *                 The order of insertion determines the priority of the
     *                 commands.
     */
    public Branch(LinkedHashMap<BooleanSupplier, Command> commands) {
        this.commands = commands;
    }

    /**
     * Starts the command by evaluating the boolean suppliers in order and
     * adopting the first command whose condition is true.
     * If none of the conditions are true, the command finishes immediately.
     * Not to be called directly, use a scheduler instead.
     */
    @Override
    public void start() {
        for (Map.Entry<BooleanSupplier, Command> entry : commands.entrySet()) {
            if (entry.getKey().getAsBoolean()) {
                adoptBehavior(entry.getValue());
                return;
            }
        }
        setDone(() -> true);
    }

    /**
     * Creates a copy of this Optional command.
     * 
     * @return a new Optional command with copies of the original commands
     */
    @Override
    public Branch copy() {
        LinkedHashMap<BooleanSupplier, Command> copiedCommands = new LinkedHashMap<>();
        for (Map.Entry<BooleanSupplier, Command> entry : commands.entrySet()) {
            copiedCommands.put(entry.getKey(), entry.getValue().copy());
        }
        return new Branch(copiedCommands);
    }
}
