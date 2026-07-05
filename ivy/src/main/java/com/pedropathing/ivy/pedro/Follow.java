package com.pedropathing.ivy.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.Path;

/**
 * A command that makes the Pedro follower follow a specified path.
 *
 * @version 1.0
 * @author Baron Henderson
 * @author Havish Sripada
 */
class Follow extends CommandBuilder {
    private final Follower follower;
    private final Path path;

    /**
     * Constructs a new Follow command that makes the given Follower follow the
     * specified path.
     *
     * @param f         The Follower to follow the path
     * @param pathChain The PathChain to follow
     */
    public Follow(Follower f, Path pathChain) {
        this.follower = f;
        this.path = pathChain;
        initialize();
    }

    private void initialize() {
        setStart(() -> follower.follow(path));
        setDone(() -> !follower.following());
    }
}
