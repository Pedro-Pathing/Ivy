package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;

public class Instant extends Command {
    public Instant(Runnable r) {
        super.setStart(r);
        super.setDone(() -> true);
    }
}
