package com.pedropathing.ivy;

import com.pedropathing.ivy.commands.Wait;
import com.pedropathing.ivy.groups.Parallel;
import com.pedropathing.ivy.groups.Race;
import com.pedropathing.ivy.groups.Sequential;

import java.util.List;

public interface ICommand {
    List<Object> getRequirements();

    Chainability getChainability();

    Interruptibility getInterruptibility();

    void start();

    boolean done();

    void execute();

    void end(boolean interrupted);

    ICommand copy();

    default Sequential then(ICommand then) {
        return new Sequential(this, then);
    }

    default Parallel with(ICommand with) {
        return new Parallel(this, with);
    }

    default Race timeoutAfter(int milliseconds) {
        return new Race(
                this,
                new Wait(milliseconds));
    }
}
