package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.CommandBuilder;

import java.util.function.BooleanSupplier;

/**
 * A command that waits until a given condition is true before finishing.
 * 
 * @version 1.0
 * @author Kabir Goyal
 */
public class WaitUntil extends CommandBuilder {
    /**
     * Constructs a new WaitUntil command that waits until the given condition
     * is true before finishing.
     *
     * @param condition the condition to wait for
     */
    public WaitUntil(BooleanSupplier condition) {
        setDone(condition);
    }
}
