package com.pedropathing.ivy.behaviors;

/**
 * Defines what happens when a command is blocked by a higher priority command.
 *
 * @version 1.0
 */
public enum BlockedBehavior {
    /**
     * The command is canceled and will not run.
     */
    CANCEL,

    /**
     * The command is queued and will run when the blocking command finishes.
     */
    QUEUE
}
