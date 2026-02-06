package com.pedropathing.ivy.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathConstraints;

/**
 * A command that makes the Pedro follower turn to a specified heading.
 *
 * @version 1.0
 * @author Baron Henderson
 * @author Havish Sripada
 * @author Kabir Goyal
 */
class Turn extends CommandBuilder {

    /**
     * Constructs a new Turn command that makes the given Follower turn to the
     * specified heading.
     *
     * @param follower The Follower to turn
     * @param radians  The heading to turn to in radians
     */
    public Turn(Follower follower, double radians) {
        this(follower, radians, follower.pathConstraints);
    }

    /**
     * Constructs a new Turn command that makes the given Follower turn to the
     * specified heading.
     *
     * @param follower    The Follower to turn
     * @param radians     The heading to turn to in radians
     * @param constraints The error allowed to consider the turn complete
     */
    public Turn(Follower follower, double radians, PathConstraints constraints) {
        setStart(() -> {
            Pose pose = follower.getPose();
            Path path = new Path(new BezierPoint(pose));
            path.setHeadingInterpolation(HeadingInterpolator.constant(radians));
            path.setConstraints(constraints);
            follower.followPath(path);
        });

        setDone(() -> !follower.isBusy());
    }
}
