package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;

/**
 * A command that runs a given runnable instantly and then finishes.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public class Instant extends Command {
    /**
     * Constructs a new Instant command that runs the given runnable
     * instantly and then finishes.
     * 
     * @param r the runnable (void function with no parameters) to execute
     */
    public Instant(Runnable r) {
        super.setStart(r);
        super.setDone(() -> true);
    }
}
