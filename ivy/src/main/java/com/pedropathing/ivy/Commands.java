package com.pedropathing.ivy;

import com.pedropathing.ivy.groups.Deadline;
import com.pedropathing.ivy.groups.Parallel;
import com.pedropathing.ivy.groups.Race;
import com.pedropathing.ivy.groups.Sequential;

public final class Commands {
    public static CommandBuilder sequential(Command... commands) {
        return new Sequential(commands);
    }

    public static CommandBuilder parallel(Command... commands) {
        return new Parallel(commands);
    }

    public static CommandBuilder race(Command... commands) {
        return new Race(commands);
    }

    public static CommandBuilder deadline(Command deadline, Command... commands) {
        return new Deadline(deadline, commands);
    }
}
