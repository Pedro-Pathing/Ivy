package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.ICommand;
import java.util.function.BooleanSupplier;

public class Conditional extends Command {
    private final BooleanSupplier decider;
    private final ICommand option1;
    private final ICommand option2;

    public Conditional(BooleanSupplier decider, ICommand ifTrue, ICommand ifFalse) {
        this.decider = decider;
        this.option1 = ifTrue;
        this.option2 = ifFalse;
    }

    @Override
    public void start() {
        if (decider.getAsBoolean()) {
            adoptBehavior(option1);
            return;
        }

        adoptBehavior(option2);
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
    public Conditional copy() {
        return new Conditional(decider, option1.copy(), option2.copy());
    }
}
