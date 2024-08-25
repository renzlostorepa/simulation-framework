package part2.simulationframework.common;

import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer<TOPIC, ITEM> {

    private LinkedList<Pair<TOPIC, ITEM>> buffer;
    private Lock mutex;
    private Condition notEmptyOrClosed, notFull;
    private boolean closed;
    private int maxSize;

    public BoundedBuffer(int size) {
        this.maxSize = size;
        buffer = new LinkedList<>();
        mutex = new ReentrantLock();
        notEmptyOrClosed = mutex.newCondition();
        notFull = mutex.newCondition();
        closed = false;
    }

    public void put(Pair<TOPIC, ITEM> item) throws InterruptedException {
        try {
            mutex.lock();
            while (isFull()) {
                notFull.await();
            }
            buffer.addLast(item);
            notEmptyOrClosed.signalAll();
        } finally {
            mutex.unlock();
        }
    }

    public Pair<TOPIC, ITEM> get() throws InterruptedException {
        try {
            mutex.lock();
            while (isEmpty() && !closed) {
                try {
                    notEmptyOrClosed.await();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (isEmpty()) {
                throw new InterruptedException();
            }
            Pair<TOPIC, ITEM> item = buffer.removeFirst();
            notFull.signalAll();
            return item;
        } finally {
            mutex.unlock();
        }
    }

    public void close() {
        try {
            mutex.lock();
            closed = true;
            notEmptyOrClosed.signalAll();
        } finally {
            mutex.unlock();
        }
    }


    private boolean isFull() {
        return buffer.size() == maxSize;
    }

    private boolean isEmpty() {
        return buffer.size() == 0;
    }

}
