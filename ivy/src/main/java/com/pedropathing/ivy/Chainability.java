package com.pedropathing.ivy;

/**
 * Enum representing whether a command will wait for conflicting UNINTERRUPTIBLE
 * commands to finish before starting or be discarded
 * 
 * @version 1.0
 * @author Kabir Goyal
 */
public enum Chainability {
    /**
     * Indicates that the command can will wait for conflicting UNINTERRUPTIBLE
     * commands to finish before starting.
     */
    CHAINABLE,
    /**
     * Indicates that the command will be discarded if there are conflicting
     * UNINTERRUPTIBLE commands running at time of scheduling.
     */
    UNCHAINABLE
}
