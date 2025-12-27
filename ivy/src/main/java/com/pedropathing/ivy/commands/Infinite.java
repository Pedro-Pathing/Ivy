package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;

/**
 * A command that runs indefinitely until interrupted.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public class Infinite extends Command {
    /**
     * Constructs a new Infinite command that runs the given runnable
     * indefinitely until interrupted.
     * 
     * @param r the runnable (void function with no parameters) to execute
     */
    public Infinite(Runnable r) {
        super.setExecute(r);
        super.setDone(() -> false);
    }
}
