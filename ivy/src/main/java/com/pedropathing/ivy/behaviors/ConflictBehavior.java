package com.pedropathing.ivy.behaviors;

/**
 * Defines what happens when a command conflicts with an equal priority command
 * that is currently running.
 *
 * @version 1.0
 */
public enum ConflictBehavior {
    /**
     * The command is canceled and will not run.
     */
    CANCEL,

    /**
     * The conflicting command is interrupted and this command runs.
     */
    OVERRIDE,

    /**
     * The command is queued and will run when the conflicting command finishes.
     */
    QUEUE
}
