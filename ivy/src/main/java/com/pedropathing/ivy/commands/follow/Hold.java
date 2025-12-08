package com.pedropathing.ivy.commands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.paths.PathConstraints;

public class Hold extends Command {
    public Hold(Follower follower) {
        this(follower, false, follower.pathConstraints);
    }

    public Hold(Follower follower, boolean useSlowMode) {
        this(follower, useSlowMode, follower.pathConstraints);
    }

    public Hold(Follower follower, PathConstraints pathConstraints) {
        this(follower, false, pathConstraints);
    }

    public Hold(Follower follower, boolean useSlowMode, PathConstraints constraints) {
        this(follower, follower.getPose(), useSlowMode, constraints);
    }

    public Hold(Follower follower, Pose pose, boolean useSlowMode) {
        this(follower, pose, useSlowMode, follower.pathConstraints);
    }

    public Hold(Follower follower, Pose pose, PathConstraints constraints) {
        this(follower, pose, false, constraints);
    }

    public Hold(Follower follower, Pose pose) {
        this(follower, pose, false, follower.pathConstraints);
    }

    public Hold(Follower follower, Pose pose, boolean useSlowMode, PathConstraints constraints) {
        setStart(() -> follower.holdPoint(new BezierPoint(pose), follower.getHeading(), useSlowMode));
        setDone(() -> follower.getTranslationalError().getMagnitude() < constraints.getTranslationalConstraint() &&
                follower.getHeadingError() < constraints.getHeadingConstraint());
    }
}
