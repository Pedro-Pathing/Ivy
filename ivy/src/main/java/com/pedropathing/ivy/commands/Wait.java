package com.pedropathing.ivy.commands;

import com.pedropathing.util.Timer;

/**
 * A command that waits for a specified amount of time before finishing.
 * 
 * @version 1.0
 * @author Baron Henderson
 * @author Kabir Goyal
 */
public class Wait extends CommandClass {
    Timer timer = new Timer();

    /**
     * Constructs a new Wait command that waits for the specified amount of time
     * before finishing.
     * 
     * @param milliseconds the amount of time to wait in milliseconds
     */
    public Wait(double milliseconds) {
        super.setStart(() -> timer.resetTimer());
        super.setDone(() -> timer.getElapsedTime() >= milliseconds);
    }
}
