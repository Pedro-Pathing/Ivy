package com.pedropathing.ivy.behaviors;

/**
 * Defines what happens when a command is interrupted by another command.
 *
 * @version 1.0
 */
public enum InterruptedBehavior {
    /**
     * The command ends permanently when interrupted.
     */
    END,

    /**
     * The command is suspended and will resume when resources become available.
     */
    SUSPEND
}
