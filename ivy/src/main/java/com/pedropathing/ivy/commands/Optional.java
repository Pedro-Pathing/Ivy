package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.ICommand;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class Optional extends Command {
    private final LinkedHashMap<BooleanSupplier, ICommand> commands;

    public Optional(LinkedHashMap<BooleanSupplier, ICommand> commands) {
        this.commands = commands;
    }

    @Override
    public void start() {
        for (Map.Entry<BooleanSupplier, ICommand> entry : commands.entrySet()) {
            if (entry.getKey().getAsBoolean()) {
                adoptBehavior(entry.getValue());
                return;
            }
        }
        setDone(() -> true);
    }

    private void adoptBehavior(ICommand v) {
        v.start();
        setExecute(v::execute);
        setEnd(v::end);
        setDone(v::done);
        setInterruptibility(v.getInterruptibility());
        setChainability(v.getChainability());
        if (v.getRequirements() != null)
            setRequirements(v.getRequirements().toArray());
    }

    @Override
    public Optional copy() {
        LinkedHashMap<BooleanSupplier, ICommand> copiedCommands = new LinkedHashMap<>();
        for (Map.Entry<BooleanSupplier, ICommand> entry : commands.entrySet()) {
            copiedCommands.put(entry.getKey(), entry.getValue().copy());
        }
        return new Optional(copiedCommands);
    }
}
