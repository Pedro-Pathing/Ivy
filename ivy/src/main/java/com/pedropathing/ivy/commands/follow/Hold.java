package com.pedropathing.ivy.commands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;

/**
 * A command that makes the Pedro follower hold a specified point.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Havish Sripada
 */
public class Hold extends CommandClass {
    /**
     * Constructs a new Hold command that makes the given Follower hold its
     * current position.
     * 
     * @param follower The Follower to hold position
     */
    public Hold(Follower follower) {
        this(follower, false, follower.pathConstraints);
    }

    /**
     * Constructs a new Hold command that makes the given Follower hold its
     * current position.
     * 
     * @param follower    The Follower to hold position
     * @param useSlowMode Whether to use slow mode while holding
     */
    public Hold(Follower follower, boolean useSlowMode) {
        this(follower, useSlowMode, follower.pathConstraints);
    }

    /**
     * Constructs a new Hold command that makes the given Follower hold its
     * current position.
     * 
     * @param follower        The Follower to hold position
     * @param pathConstraints The path constraints to use while holding
     */
    public Hold(Follower follower, PathConstraints pathConstraints) {
        this(follower, false, pathConstraints);
    }

    /**
     * Constructs a new Hold command that makes the given Follower hold its
     * current position.
     * 
     * @param follower        The Follower to hold position
     * @param useSlowMode     Whether to use slow mode while holding
     * @param pathConstraints The path constraints to use while holding
     */
    public Hold(Follower follower, boolean useSlowMode, PathConstraints constraints) {
        this(follower, follower.getPose(), useSlowMode, constraints);
    }

    /**
     * Constructs a new Hold command that makes the given Follower hold the
     * specified position.
     * 
     * @param follower The Follower to hold position
     * @param pose     The Pose to hold
     */
    public Hold(Follower follower, Pose pose, boolean useSlowMode) {
        this(follower, pose, useSlowMode, follower.pathConstraints);
    }

    /**
     * Constructs a new Hold command that makes the given Follower hold the
     * specified position.
     * 
     * @param follower    The Follower to hold position
     * @param pose        The Pose to hold
     * @param constraints The error allowed to consider the hold complete
     */
    public Hold(Follower follower, Pose pose, PathConstraints constraints) {
        this(follower, pose, false, constraints);
    }

    /**
     * Constructs a new Hold command that makes the given Follower hold the
     * specified position.
     * 
     * @param follower The Follower to hold position
     * @param pose     The Pose to hold
     */
    public Hold(Follower follower, Pose pose) {
        this(follower, pose, false, follower.pathConstraints);
    }

    /**
     * Constructs a new Hold command that makes the given Follower hold the
     * specified position.
     * 
     * @param follower    The Follower to hold position
     * @param pose        The Pose to hold
     * @param useSlowMode Whether to use slow mode while holding
     * @param constraints The error allowed to consider the hold complete
     */
    public Hold(Follower follower, Pose pose, boolean useSlowMode, PathConstraints constraints) {
        setStart(() -> follower.holdPoint(new BezierPoint(pose), follower.getHeading(), useSlowMode));
        setDone(() -> follower.getTranslationalError().getMagnitude() < constraints.getTranslationalConstraint() &&
                follower.getHeadingError() < constraints.getHeadingConstraint());
    }
}
