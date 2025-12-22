package com.pedropathing.ivy.groups;

import com.pedropathing.ivy.ICommand;
import com.pedropathing.ivy.Interruptibility;
import com.pedropathing.ivy.Chainability;

import java.util.*;

public class Sequential implements ICommand {
  protected ArrayDeque<ICommand> commands = new ArrayDeque<>();
  protected List<Object> requirements = new ArrayList<>();
  private Interruptibility interruptibility = Interruptibility.INTERRUPTIBLE;
  private Chainability chainability = Chainability.UNCHAINABLE;

  public Sequential(ICommand... cmds) {
    commands.addAll(Arrays.asList(cmds));
    rebuildRequirements();
    generateInterruptibility();
  }

  public Sequential() {
  }

  @Override
  public void execute() {
    if (done())
      return;

    ICommand current = commands.peek();
    assert current != null;

    if (current.done()) {
      current.end(false);
      commands.poll();
      if (!done()) {
        ICommand next = commands.peek();
        if (next != null)
          next.start();
      }
    } else {
      current.execute();
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
    while (!commands.isEmpty()) {
      ICommand c = commands.poll();
      if (c != null) {
        c.end(interrupted);
      }
    }
  }

  @Override
  public ICommand copy() {
    ICommand[] cmds = new ICommand[commands.size()];
    int i = 0;
    for (ICommand command : commands) {
      cmds[i++] = command.copy();
    }
    return new Sequential(cmds).setChainability(chainability);
  }

  @Override
  public void start() {
    if (!done()) {
      ICommand current = commands.peek();
      if (current != null)
        current.start();
    }
  }

  @Override
  public boolean done() {
    return commands.isEmpty();
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

  public Chainability getChainability() {
    return chainability;
  }

  public Sequential setChainability(Chainability chainability) {
    this.chainability = chainability;
    return this;
  }
}
