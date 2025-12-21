package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;

import java.util.*;

public class Race implements ICommand {
  private LinkedList<ICommand> commands = new LinkedList<>();
  private List<Object> requirements = new ArrayList<>();
  protected boolean raceCompleted = false;
  private Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;

  public Race(ICommand... cmds) {
    commands.addAll(Arrays.asList(cmds));
    rebuildRequirements();
    generateInterruptibility();
  }

  @Override
  public void execute() {
    if (!done()) {
      Iterator<ICommand> it = commands.iterator();
      ICommand winner = null;
      while (it.hasNext()) {
        ICommand command = it.next();
        if (command.done()) {
          raceCompleted = true;
          command.end(false);
          winner = command;
          break;
        } else {
          command.execute();
        }
      }

      if (raceCompleted) {
        for (ICommand cmd : commands) {
          if (cmd != winner) {
            cmd.end(true);
          }
        }
      }
    }
  }

  @Override
  public List<Object> getRequirements() {
    return requirements;
  }

  @Override
  public Interruptibility getInterruptibility() {
    return interruptibility;
  }

  protected void generateInterruptibility() {
    for (ICommand command : commands) {
      if (command.getInterruptibility() == Interruptibility.UNINTERRUPTIBLE) {
        interruptibility = Interruptibility.UNINTERRUPTIBLE;
        return;
      }
    }
  }

  @Override
  public void end(boolean interrupted) {
    for (ICommand command : commands) {
      command.end(interrupted);
    }
    commands.clear();
  }

  @Override
  public ICommand copy() {
    ICommand[] cmds = new ICommand[commands.size()];
    int i = 0;
    for (ICommand command : commands) {
      cmds[i++] = command.copy();
    }
    return new Race(cmds);
  }

  @Override
  public void start() {
    for (ICommand command : commands) {
      command.start();
    }
  }

  protected void rebuildRequirements() {
    Set<Object> set = new HashSet<>();
    for (ICommand command : commands) {
      List<Object> r = command.getRequirements();
      if (r != null)
        set.addAll(r);
    }
    requirements = new ArrayList<>(set);
  }

  @Override
  public boolean done() {
    return raceCompleted;
  }
}
