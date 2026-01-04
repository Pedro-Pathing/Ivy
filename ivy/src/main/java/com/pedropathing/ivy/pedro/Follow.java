package com.pedropathing.ivy.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.PathChain;

/**
 * A command that makes the Pedro follower follow a specified path.
 *
 * @version 1.0
 * @author Baron Henderson
 * @author Havish Sripada
 */
class Follow extends CommandBuilder {
    private final Follower follower;
    private final PathChain path;
    private boolean holdEnd;
    private double maxPower;

    /**
     * Constructs a new Follow command that makes the given Follower follow the
     * specified path.
     *
     * @param f         The Follower to follow the path
     * @param pathChain The PathChain to follow
     */
    public Follow(Follower f, PathChain pathChain) {
        this.follower = f;
        this.path = pathChain;
        maxPower = follower.getMaxPowerScaling();
        holdEnd = follower.constants.automaticHoldEnd;
        initialize();
    }

    /**
     * Constructs a new Follow command that makes the given Follower follow the
     * specified path.
     *
     * @param f         The Follower to follow the path
     * @param pathChain The PathChain to follow
     * @param maxPower  The maximum power the follower can use (between 0 and 1)
     */
    public Follow(Follower f, PathChain pathChain, double maxPower) {
        this.follower = f;
        this.path = pathChain;
        this.maxPower = maxPower;
        holdEnd = follower.constants.automaticHoldEnd;
        initialize();
    }

    /**
     * Constructs a new Follow command that makes the given Follower follow the
     * specified path.
     *
     * @param f         The Follower to follow the path
     * @param pathChain The PathChain to follow
     * @param holdEnd   If the robot should maintain its ending position
     */
    public Follow(Follower f, PathChain pathChain, boolean holdEnd) {
        this.follower = f;
        this.path = pathChain;
        this.holdEnd = holdEnd;
        maxPower = follower.getMaxPowerScaling();
        initialize();
    }

    /**
     * Constructs a new Follow command that makes the given Follower follow the
     * specified path.
     *
     * @param f         The Follower to follow the path
     * @param pathChain The PathChain to follow
     * @param holdEnd   If the robot should maintain its ending position
     * @param maxPower  The maximum power the follower can use (between 0 and 1)
     */
    public Follow(Follower f, PathChain pathChain, boolean holdEnd, double maxPower) {
        this.follower = f;
        this.path = pathChain;
        this.holdEnd = holdEnd;
        this.maxPower = maxPower;
        initialize();
    }

    private void initialize() {
        setStart(() -> follower.followPath(path, maxPower, holdEnd));
        setDone(() -> !follower.isBusy());
    }
}
