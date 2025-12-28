package com.pedropathing.ivy.commands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;

/**
 * A command that makes the Pedro follower follow a specified path.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Havish Sripada
 */
public class Follow extends CommandClass {
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
    }

    /**
     * Decides whether or not to make the robot maintain its position once the path
     * ends.
     *
     * @param holdEnd If the robot should maintain its ending position
     * @return This command so setters can be chained
     */
    public Follow setHoldEnd(boolean holdEnd) {
        this.holdEnd = holdEnd;
        return this;
    }

    /**
     * Sets the follower's maximum power
     * 
     * @param power Between 0 and 1
     * @return This command so setters can be chained
     */
    public Follow setMaxPower(double power) {
        this.maxPower = power;
        return this;
    }

    /**
     * Starts the follower following the path
     */
    @Override
    public void start() {
        follower.followPath(path, maxPower, holdEnd);
    }

    /**
     * @return whether the follower has finished following the path
     */
    @Override
    public boolean done() {
        return !follower.isBusy();
    }

    /**
     * Ends the follower's path following
     * 
     * @param interrupted whether the command was interrupted or ended normally
     */
    @Override
    public void end(boolean interrupted) {
    }
}
