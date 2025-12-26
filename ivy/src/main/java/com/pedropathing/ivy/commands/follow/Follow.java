package com.pedropathing.ivy.commands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.paths.PathChain;

public class Follow extends Command {
    private final Follower follower;
    private final PathChain path;
    private boolean holdEnd;
    private double maxPower;

    public Follow(Follower f, PathChain pathChain) {
        this.follower = f;
        this.path = pathChain;
        maxPower = follower.getMaxPowerScaling();
        holdEnd = follower.constants.automaticHoldEnd;
    }

    public Follow(Follower f, PathChain pathChain, double maxPower) {
        this.follower = f;
        this.path = pathChain;
        this.maxPower = maxPower;
        holdEnd = follower.constants.automaticHoldEnd;
    }

    public Follow(Follower f, PathChain pathChain, boolean holdEnd) {
        this.follower = f;
        this.path = pathChain;
        this.holdEnd = holdEnd;
        maxPower = follower.getMaxPowerScaling();
    }

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
     * @return This command for compatibility in command groups
     */
    public Follow setHoldEnd(boolean holdEnd) {
        this.holdEnd = holdEnd;
        return this;
    }

    /**
     * Sets the follower's maximum power
     * 
     * @param power Between 0 and 1
     * @return This command for compatibility in command groups
     */
    public Follow setMaxPower(double power) {
        this.maxPower = power;
        return this;
    }

    public void start() {
        follower.followPath(path, maxPower, holdEnd);
    }

    @Override
    public boolean done() {
        return !follower.isBusy();
    }

    @Override
    public void end(boolean interrupted) {
    }
}
