package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;

import java.util.function.BooleanSupplier;

/**
 * A command that waits until a given condition is true before finishing.
 * 
 * @version 1.0
 * @author Kabir Goyal
 */
public class WaitUntil extends Command {
    /**
     * Constructs a new WaitUntil command that waits until the given condition
     * is true before finishing.
     * 
     * @param supplier the condition to wait for
     */
    public WaitUntil(BooleanSupplier supplier) {
        super.setDone(supplier);
    }
}
