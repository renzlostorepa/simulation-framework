package part1.simulationframework.common;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ResettableCountdownLatch {

    private final int initialCount;
    private CountDownLatch latch;

    public ResettableCountdownLatch(int count) {
        initialCount = count;
        latch = new CountDownLatch(count);
    }

    public void reset() {
        latch = new CountDownLatch(initialCount);
    }

    public synchronized void countDown() {
        latch.countDown();
    }

    public void await() throws InterruptedException {
        latch.await();
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }
}
