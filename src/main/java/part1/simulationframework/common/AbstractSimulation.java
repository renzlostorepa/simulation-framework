package part1.simulationframework.common;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import part1.simulationframework.contracts.SimulationListener;
import part1.simulationframework.simtrafficbase.CarInfo;

import java.util.ArrayList;
import java.util.List;

import static part1.simulationframework.common.WorkerTopic.ACT;
import static part1.simulationframework.common.WorkerTopic.SENSE_AND_DECIDE;

/**
 * Base class for defining concrete simulations
 */
public abstract class AbstractSimulation {

    /* environment of the simulation */
    private AbstractEnvironment env;

    /* list of the agents */
    protected List<CarInfo> agents;
    /* simulation listeners */
    private List<SimulationListener> listeners;

    /* logical time step */
    protected int dt;

    /* initial logical time */
    private int t0;

    protected int numOfWorkers;
    protected BoundedBuffer<WorkerTopic, CarInfo> bagOfTasks;
    protected ResettableCountdownLatch latch;

    private TimeStatisticsState timeStatisticsState;


    protected AbstractSimulation() {
        agents = new ArrayList<CarInfo>();
        listeners = new ArrayList<SimulationListener>();
        timeStatisticsState = new TimeStatisticsState();
        numOfWorkers = Runtime.getRuntime().availableProcessors();
        bagOfTasks = new BoundedBuffer<>(numOfWorkers * 2);
    }

    /**
     * Method used to configure the simulation, specifying env and agents
     */
    protected abstract void setup();

    /**
     * Method running the simulation for a number of steps,
     * using a sequential approach
     *
     * @param numSteps
     */
    public void run(int numSteps) throws InterruptedException {

        this.timeStatisticsState.startWallTime();

        /* initialize the env and the agents inside */
        int t = t0;

        env.init();

        this.notifyReset(t, agents, env);

        int nSteps = 0;

        while (nSteps < numSteps) {

            this.timeStatisticsState.setCurrentWallTime();

            /* make a step */

            env.step(dt);

            for (var agent : agents) {
                bagOfTasks.put(new ImmutablePair(SENSE_AND_DECIDE, agent));
            }
            /* WAIT FOR WORKERS TO COMPLETE */
            latch.await();
            /* RESET LATCH COUNTER */
            latch.reset();

            for (var agent : agents) {
                bagOfTasks.put(new ImmutablePair(ACT, agent));
            }
            /* WAIT FOR WORKERS TO COMPLETE */
            latch.await();
            /* RESET LATCH COUNTER */
            latch.reset();

            t += dt;
            notifyNewStep(t, agents, env);

            nSteps++;
            this.timeStatisticsState.updateTimePerStep();

            if (this.timeStatisticsState.isToBeInSyncWithWallTime()) {
                this.timeStatisticsState.syncWithWallTime();
            }
        }

        this.timeStatisticsState.setEndWallTime(numSteps);
        bagOfTasks.close();
    }

    public TimeStatisticsState getTimeStatisticsState() {
        return timeStatisticsState;
    }

    /* methods for configuring the simulation */

    protected void setupTimings(int t0, int dt) {
        this.dt = dt;
        this.t0 = t0;
    }

    protected void syncWithTime(int nCyclesPerSec) {
        this.timeStatisticsState.syncWithTime(nCyclesPerSec);
    }

    protected void setupEnvironment(AbstractEnvironment env) {
        this.env = env;
    }

    protected void addAgent(CarInfo agent) {
        agents.add(agent);
    }

    /* methods for listeners */

    public void addSimulationListener(SimulationListener l) {
        this.listeners.add(l);
    }

    private void notifyReset(int t0, List<CarInfo> agents, AbstractEnvironment env) {
        for (var l : listeners) {
            l.notifyInit(t0, agents, env);
        }
    }

    private void notifyNewStep(int t, List<CarInfo> agents, AbstractEnvironment env) {
        for (var l : listeners) {
            l.notifyStepDone(t, agents, env);
        }
    }

}
