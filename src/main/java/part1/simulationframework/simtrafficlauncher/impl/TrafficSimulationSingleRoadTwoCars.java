package part1.simulationframework.simtrafficlauncher.impl;

import part1.simulationframework.common.AbstractSimulation;
import part1.simulationframework.common.ResettableCountdownLatch;
import part1.simulationframework.simtrafficbase.*;

public class TrafficSimulationSingleRoadTwoCars extends AbstractSimulation {

    public TrafficSimulationSingleRoadTwoCars() {
        super();
    }

    public void setup() {

        int t0 = 0;
        int dt = 1;

        this.setupTimings(t0, dt);

        RoadsEnv env = new RoadsEnv();
        this.setupEnvironment(env);

        Road r = env.createRoad(new P2d(0, 300), new P2d(1500, 300));
        CarInfo car1 = new CarInfo("car-1", r, 0, 0.1, 0.2, 8);
        env.registerNewCar(car1.getCarName(), car1);
        this.addAgent(car1);
        CarInfo car2 = new CarInfo("car-2", r, 100, 0.1, 0.1, 7);
        env.registerNewCar(car2.getCarName(), car2);
        this.addAgent(car2);
        latch = new ResettableCountdownLatch(agents.size());
        for (int i = 0; i < this.numOfWorkers; i++) {
            new CarBasicWorker("w-" + i, bagOfTasks, latch, env, dt).start();
        }

        /* sync with wall-time: 25 steps per sec */
        this.syncWithTime(25);
    }

}

