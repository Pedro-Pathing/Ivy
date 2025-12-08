package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;

public class Infinite extends Command {
    public Infinite(Runnable r) {
        super.setExecute(r);
        super.setDone(() -> false);
    }
}
