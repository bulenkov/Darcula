package com.bulenkov.darcula.util;

import javax.swing.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Konstantin Bulenkov
 */
public abstract class Animator {
  private final static ScheduledExecutorService scheduler = createScheduler();

  private static ScheduledExecutorService createScheduler() {
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
      public Thread newThread(final Runnable r) {
        final Thread thread = new Thread(r, "Darcula Animations");
        thread.setDaemon(true);
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
      }
    });
    executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
    executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
    return executor;

  }

  private final int myTotalFrames;
  private final int myCycleDuration;
  private final boolean myForward;
  private final boolean myRepeatable;

  private ScheduledFuture<?> myTicker;

  private int myCurrentFrame;
  private long myStartTime;
  private long myStopTime;
  private volatile boolean myDisposed = false;

  public Animator(final String name,
                  final int totalFrames,
                  final int cycleDuration,
                  boolean repeatable) {

    this(name, totalFrames, cycleDuration, repeatable, true);
  }

  public Animator(final String name,
                  final int totalFrames,
                  final int cycleDuration,
                  boolean repeatable,
                  boolean forward) {
    myTotalFrames = totalFrames;
    myCycleDuration = cycleDuration;
    myRepeatable = repeatable;
    myForward = forward;
    myCurrentFrame = forward ? 0 : totalFrames;

    reset();
  }

  private void onTick() {
    if (isDisposed()) return;

    if (myStartTime == -1) {
      myStartTime = System.currentTimeMillis();
      myStopTime = myStartTime + myCycleDuration * (myTotalFrames - myCurrentFrame) / myTotalFrames;
    }

    final double passedTime = System.currentTimeMillis() - myStartTime;
    final double totalTime = myStopTime - myStartTime;

    final int newFrame = (int)(passedTime * myTotalFrames / totalTime);
    if (myCurrentFrame > 0 && newFrame == myCurrentFrame) return;
    myCurrentFrame = newFrame;

    if (myCurrentFrame >= myTotalFrames) {
      if (myRepeatable) {
        reset();
      }
      else {
        animationDone();
        return;
      }
    }

    paint();
  }

  private void paint() {
    paintNow(myForward ? myCurrentFrame : myTotalFrames - myCurrentFrame - 1, myTotalFrames, myCycleDuration);
  }

  private void animationDone() {
    stopTicker();

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        paintCycleEnd();
      }
    });
  }

  private void stopTicker() {
    if (myTicker != null) {
      myTicker.cancel(false);
      myTicker = null;
    }
  }

  protected void paintCycleEnd() {

  }

  public void suspend() {
    myStartTime = -1;
    stopTicker();
  }

  public void resume() {
    if (myCycleDuration == 0) {
      myCurrentFrame = myTotalFrames - 1;
      paint();
      animationDone();
    }
    else if (myTicker == null) {
      myTicker = scheduler.scheduleWithFixedDelay(new Runnable() {
        AtomicBoolean scheduled = new AtomicBoolean(false);

        @Override
        public void run() {
          if (scheduled.compareAndSet(false, true) && !isDisposed()) {
            SwingUtilities.invokeLater(new Runnable() {
              @Override
              public void run() {
                scheduled.set(false);
                onTick();
              }
            });
          }
        }
      }, 0, myCycleDuration * 1000 / myTotalFrames, TimeUnit.MICROSECONDS);
    }
  }

  public abstract void paintNow(int frame, int totalFrames, int cycle);

  public void dispose() {
    myDisposed = true;
    stopTicker();
  }

  public boolean isRunning() {
    return myTicker != null;
  }

  public void reset() {
    myCurrentFrame = 0;
    myStartTime = -1;
  }

  public final boolean isForward() {
    return myForward;
  }

  public boolean isDisposed() {
    return myDisposed;
  }
}
