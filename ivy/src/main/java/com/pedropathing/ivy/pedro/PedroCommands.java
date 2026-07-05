package com.pedropathing.ivy.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.Path;

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
    public static CommandBuilder follow(Follower follower, Path pathChain) {
        return new Follow(follower, pathChain);
    }
}
