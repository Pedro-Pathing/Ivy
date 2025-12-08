package com.pedropathing.ivy;

import com.pedropathing.ivy.groups.Sequential;

public class Test {

    Command testCommand = new Command()
            .setExecute(() -> {})
            .setDone(() -> true)
            .setStart(() -> {})
            .setEnd(() -> {});

    Sequential testSequential = new Sequential(testCommand, testCommand);

}
