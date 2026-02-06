package com.pedropathing.ivy.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.PathConstraints;

public final class PedroCommands {
    private PedroCommands() {
    }

    /**
     * Creates a command that makes the follower follow a path.
     *
     * @param follower  the follower to control
     * @param pathChain the path to follow
     * @return a new Follow command
     */
    public static CommandBuilder follow(Follower follower, PathChain pathChain) {
        return new Follow(follower, pathChain);
    }

    /**
     * Creates a command that makes the follower follow a path with specified max power.
     *
     * @param follower  the follower to control
     * @param pathChain the path to follow
     * @param maxPower  the maximum power (between 0 and 1)
     * @return a new Follow command
     */
    public static CommandBuilder follow(Follower follower, PathChain pathChain, double maxPower) {
        return new Follow(follower, pathChain, maxPower);
    }

    /**
     * Creates a command that makes the follower follow a path with hold end option.
     *
     * @param follower  the follower to control
     * @param pathChain the path to follow
     * @param holdEnd   whether to hold position at the end
     * @return a new Follow command
     */
    public static CommandBuilder follow(Follower follower, PathChain pathChain, boolean holdEnd) {
        return new Follow(follower, pathChain, holdEnd);
    }

    /**
     * Creates a command that makes the follower follow a path with all options.
     *
     * @param follower  the follower to control
     * @param pathChain the path to follow
     * @param holdEnd   whether to hold position at the end
     * @param maxPower  the maximum power (between 0 and 1)
     * @return a new Follow command
     */
    public static CommandBuilder follow(Follower follower, PathChain pathChain, boolean holdEnd, double maxPower) {
        return new Follow(follower, pathChain, holdEnd, maxPower);
    }

    /**
     * Creates a command that makes the follower hold its current position.
     *
     * @param follower the follower to control
     * @return a new Hold command
     */
    public static CommandBuilder hold(Follower follower) {
        return new Hold(follower);
    }

    /**
     * Creates a command that makes the follower hold a specified pose.
     *
     * @param follower the follower to control
     * @param pose     the pose to hold
     * @return a new Hold command
     */
    public static CommandBuilder hold(Follower follower, Pose pose) {
        return new Hold(follower, pose);
    }

    /**
     * Creates a command that makes the follower hold a specified pose with constraints.
     *
     * @param follower    the follower to control
     * @param pose        the pose to hold
     * @param constraints the path constraints for completion tolerance
     * @return a new Hold command
     */
    public static CommandBuilder hold(Follower follower, Pose pose, PathConstraints constraints) {
        return new Hold(follower, pose, constraints);
    }

    /**
     * Creates a command that makes the follower turn to a specified heading.
     *
     * @param follower the follower to control
     * @param radians  the target heading in radians
     * @return a new Turn command
     */
    public static CommandBuilder turnTo(Follower follower, double radians) {
        return new Turn(follower, radians);
    }

    /**
     * Creates a command that makes the follower turn to a specified heading with constraints.
     *
     * @param follower    the follower to control
     * @param radians     the target heading in radians
     * @param constraints the path constraints for completion tolerance
     * @return a new Turn command
     */
    public static CommandBuilder turnTo(Follower follower, double radians, PathConstraints constraints) {
        return new Turn(follower, radians, constraints);
    }

}
