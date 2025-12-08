package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.util.Timer;

public class Wait extends Command {
    Timer timer = new Timer();
    public Wait(double milliseconds) {
        super.setStart(() -> timer.resetTimer());
        super.setDone(() -> timer.getElapsedTime() >= milliseconds);
    }
}
