package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Loop extends Sequential {
  public Loop(ICommand command, int iterations) {
    ICommand[] loops = new ICommand[iterations];
    for (int i = 0; i < iterations; i++) {
      loops[i] = command.copy();
    }
    commands.addAll(Arrays.asList(loops));
    rebuildRequirements();
    generateInterruptibility();
  }
}
