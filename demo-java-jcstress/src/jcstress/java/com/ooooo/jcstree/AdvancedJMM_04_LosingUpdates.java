package com.ooooo.jcstree;

import java.util.concurrent.atomic.AtomicInteger;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

/**
 * <a href="https://github.com/openjdk/jcstress/blob/master/jcstress-samples/src/main/java/org/openjdk/jcstress/samples/jmm/advanced/AdvancedJMM_04_LosingUpdates.java">github</a>
 */
public class AdvancedJMM_04_LosingUpdates {

     /*
        How to run this test:
            $ java -jar jcstress-samples/target/jcstress.jar -t AdvancedJMM_04_LosingUpdates[.SubTestName]
     */

    /*
      ----------------------------------------------------------------------------------------------------------
        This is a simple example, but it nevertheless important to see how dangerous race conditions are.
        We have already established that v++ over volatile field is not atomic. But here is a more interesting
        question: how many updates we can actually lose? Perhaps naively, many would answer that we could lose
        one update per iteration. After all, the actors always see the latest value in the "v", so we might see
        at most the old-just-before-update value. So, if we do 5 updates in every thread, we could expect that
        we would see at least 5 as the final result?
        This intuition is contradicted by this simple test:
          RESULT        SAMPLES     FREQ       EXPECT  DESCRIPTION
              10  4,314,297,607   25.10%   Acceptable  Boring
               2     19,121,833    0.11%  Interesting  Whoa
               3     65,889,475    0.38%  Interesting  Whoa
               4    167,420,625    0.97%  Interesting  Whoa
               5  2,682,284,766   15.61%   Acceptable  Okay
               6  2,033,772,928   11.83%   Acceptable  Okay
               7  2,523,123,422   14.68%   Acceptable  Okay
               8  2,847,721,682   16.57%   Acceptable  Okay
               9  2,533,299,886   14.74%   Acceptable  Okay
        The most interesting result, "2" can be explained by this interleaving:
            Thread 1: (0 ------ stalled -------> 1)     (1->2)(2->3)(3->4)(4->5)
            Thread 2:   (0->1)(1->2)(2->3)(3->4)    (1 -------- stalled ---------> 2)
        This example shows that non-synchronized counter can lose the arbitrary number of
        updates, and even revert the history!
        Exercise for the reader: prove that both "0" and "1" are impossible results.
     */

  @JCStressTest
  @State
  @Outcome(id = "10", expect = Expect.ACCEPTABLE, desc = "Boring")
  @Outcome(id = {"0", "1"}, expect = Expect.FORBIDDEN, desc = "Boring")
  @Outcome(id = {"9", "8", "7", "6", "5"}, expect = Expect.ACCEPTABLE, desc = "Okay")
  @Outcome(expect = Expect.ACCEPTABLE_INTERESTING, desc = "Whoa")
  public static class Volatiles {

    volatile int x;

    @Actor
    void actor1() {
      for (int i = 0; i < 5; i++) {
        x++;
      }
    }

    @Actor
    void actor2() {
      for (int i = 0; i < 5; i++) {
        x++;
      }
    }

    @Arbiter
    public void arbiter(I_Result r) {
      r.r1 = x;
    }
  }

    /*
      ----------------------------------------------------------------------------------------------------------
        Of course, if we do AtomicInteger, the only plausible result is "10".
          RESULT      SAMPLES     FREQ      EXPECT  DESCRIPTION
              10  638,040,064  100.00%  Acceptable  Boring
     */

  @JCStressTest
  @State
  @Outcome(id = "10", expect = Expect.ACCEPTABLE, desc = "Boring")
  @Outcome(expect = Expect.FORBIDDEN, desc = "Whoa")
  public static class Atomics {

    AtomicInteger ai = new AtomicInteger();

    @Actor
    void actor1() {
      for (int i = 0; i < 5; i++) {
        ai.incrementAndGet();
      }
    }

    @Actor
    void actor2() {
      for (int i = 0; i < 5; i++) {
        ai.incrementAndGet();
      }
    }

    @Arbiter
    public void arbiter(I_Result r) {
      r.r1 = ai.get();
    }
  }

}