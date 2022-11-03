package com.ooooo.jcstress;

import java.util.concurrent.atomic.AtomicBoolean;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

/**
 * <a href="https://github.com/openjdk/jcstress/blob/master/jcstress-samples/src/main/java/org/openjdk/jcstress/samples/primitives/mutex/Mutex_03_SpinLock.java">github</a>
 */
@JCStressTest
@Outcome(id = {"1, 2", "2, 1"}, expect = Expect.ACCEPTABLE, desc = "Mutex works")
@Outcome(id = "1, 1", expect = Expect.FORBIDDEN, desc = "Mutex failure")
@State
public class Mutex_03_SpinLock {

    /*
        How to run this test:
            $ java -jar jcstress-samples/target/jcstress.jar -t Mutex_03_SpinLock
    */

    /*
      ----------------------------------------------------------------------------------------------------------
        A mutex can be implemented with a single atomic variable, which would coordinate threads
        entering the critical section. This construction is usually known as "spinlock".
        On x86_64, AArch64, PPC64:
            RESULT      SAMPLES     FREQ      EXPECT  DESCRIPTION
              1, 1            0    0.00%   Forbidden  Mutex failure
              1, 2  299,137,894   51.69%  Acceptable  Mutex works
              2, 1  279,551,130   48.31%  Acceptable  Mutex works
     */

  private final AtomicBoolean taken = new AtomicBoolean(false);
  private int v;

  @Actor
  public void actor1(II_Result r) {
    while (taken.get() || !taken.compareAndSet(false, true))
      ; // wait
    { // critical section
      r.r1 = ++v;
    }
    taken.set(false);
  }

  @Actor
  public void actor2(II_Result r) {
    while (taken.get() || !taken.compareAndSet(false, true))
      ; // wait
    { // critical section
      r.r2 = ++v;
    }
    taken.set(false);
  }
}