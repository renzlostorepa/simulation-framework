package part2.simulationframework.common;

import org.apache.commons.lang3.tuple.Pair;
import part2.simulationframework.simtrafficbase.CarInfo;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractWorker extends Thread {

    private BoundedBuffer<WorkerTopic, CarInfo> bagOfTasks;
    private ResettableCountdownLatch latch;

    protected AbstractEnvironment env;
    protected int dt;

    public AbstractWorker(String name, BoundedBuffer<WorkerTopic, CarInfo> bagOfTasks, ResettableCountdownLatch latch, AbstractEnvironment env, int dt) {
        super(name);
        this.bagOfTasks = bagOfTasks;
        this.latch = latch;
        this.env = env;
        this.dt = dt;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Pair<WorkerTopic, CarInfo> task;
            try {
                task = bagOfTasks.get();
            } catch (InterruptedException e) {
                break;
            }
            /* Sense and Decide or Act */
            switch (task.getLeft()) {
                case SENSE_AND_DECIDE -> senseAndDecide(task.getRight());
                case ACT -> act(task.getRight());
            }
            /* Update Latch */
            latch.countDown();
        }
    }

    protected abstract void senseAndDecide(CarInfo right);

    protected abstract void act(CarInfo right);


}
