package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;

/**
 * A command group that runs a command infinitely until interrupted.
 * When the command finishes, it is automatically restarted.
 *
 * @version 1.0
 * @author Kabir Goyal
 */
class Loop extends CommandBuilder {

    /**
     * Constructs a new Loop command that runs the given command infinitely.
     *
     * @param command the command to loop indefinitely
     */
    public Loop(Command command) {

        requiring(command.requirements());

        setPriority(command.priority());

        setExecute(() -> {
            if (command.done()) {
                command.end(EndCondition.NATURALLY);
                command.start();
                return;
            }

            command.execute();
        });

        setEnd(command::end);

        setStart(command::start);

        setDone(() -> false);
    }
}
