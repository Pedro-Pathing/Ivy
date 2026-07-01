package com.pedropathing.ivy.behaviors;

/**
 * Represents the condition under which a command ended.
 *
 * @version 1.0
 */
public enum EndCondition {
    /**
     * The command completed its task normally.
     */
    NATURALLY,

    /**
     * The command was interrupted by another command or external action.
     */
    INTERRUPTED,

    /**
     * The command was suspended and may be resumed later.
     */
    SUSPENDED
}
