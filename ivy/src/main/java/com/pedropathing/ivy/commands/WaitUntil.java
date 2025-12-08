package com.pedropathing.ivy.commands;

import com.pedropathing.ivy.Command;

import java.util.function.BooleanSupplier;

public class WaitUntil extends Command {
    public WaitUntil(BooleanSupplier supplier) {
        super.setDone(supplier);
    }
}
