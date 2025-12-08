package com.pedropathing.ivy;

import java.util.List;

public interface ICommand {
    List<Object> getRequirements();
    Interruptibility getInterruptibility();
    void start();
    boolean done();
    void execute();
    void end(boolean interrupted);
    ICommand copy();
}
