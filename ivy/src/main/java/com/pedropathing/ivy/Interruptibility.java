package com.pedropathing.ivy;

/**
 * Enum representing the interruptibility of a command.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public enum Interruptibility {
    /**
     * Indicates that the command can be interrupted.
     */
    INTERRUPTIBLE,
    /**
     * Indicates that the command cannot be interrupted.
     */
    UNINTERRUPTIBLE,
}
