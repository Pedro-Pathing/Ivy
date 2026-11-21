package com.pedropathing.ivy.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import java.util.function.DoubleSupplier;

public final class PedroCommands {
    private PedroCommands() {
    }

    /**
     * Creates a command that makes the follower follow a path and finish when the follower reaches the end of the path.
     *
     * @param follower  the follower to control
     * @param path the path to follow
     * @return a new Follow command
     */
    public static CommandBuilder follow(Follower follower, Path path) {
        return new CommandBuilder()
                .setStart(() -> follower.follow(path))
                .setDone(follower::atParametricEnd);
    }

    /**
     * Creates a command that makes the follower hold its current position.
     *
     * @param follower the follower to control
     * @return a new Hold command
     */
    public static CommandBuilder hold(Follower follower) {
        return hold(follower, follower.pose());
    }

    /**
     * Creates a command that makes the follower hold a specified pose.
     *
     * @param follower the follower to control
     * @param pose     the pose to hold
     * @return a new Hold command
     */
    public static CommandBuilder hold(Follower follower, Pose pose) {
        return Commands.instant(() -> follower.hold(pose));
    }

    /**
     * Creates a command that drives the follower manually with the given powers until interrupted.
     * When it ends, the follower stops.
     *
     * @param follower the follower to control
     * @param forward  the forward power
     * @param strafe   the strafe power
     * @param turn     the turn power
     * @return a new Drive command
     */
    public static CommandBuilder drive(Follower follower, DoubleSupplier forward, DoubleSupplier strafe, DoubleSupplier turn) {
        return Commands.infinite(() -> follower.manual(forward.getAsDouble(), strafe.getAsDouble(), turn.getAsDouble()))
                .setEnd(endCondition -> follower.stop());
    }

}
